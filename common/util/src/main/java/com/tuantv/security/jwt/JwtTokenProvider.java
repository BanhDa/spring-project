package com.tuantv.security.jwt;

import com.tuantv.dto.response.ResponseCode;
import com.tuantv.exception.BusinessException;
import com.tuantv.security.config.JwtConfiguration;
import static com.tuantv.security.constant.AuthenticationKey.*;

import com.tuantv.security.constant.KeycloakRealm;
import com.tuantv.security.dto.UserPrincipal;
import com.tuantv.util.StringUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Component
@AllArgsConstructor
public class JwtTokenProvider {

    private final JwtConfiguration jwtConfiguration;

    public Authentication decodeToken(String token) {
        Claims claims = parseClaims(token);
        if (claims == null) {
            return null;
        }

        UserPrincipal userPrincipal = getUserPrincipalFromClaims(claims);
        return new UsernamePasswordAuthenticationToken(userPrincipal, token, userPrincipal.getAuthorities());
    }

    public String getCifFromToken(String token) {
        validateToken(token);
        List<String> list = (List<String>) parseClaims(token.substring(7)).get(CIF);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    public String getUsernameFromToken(String token) {
        validateToken(token);
        Object name = parseClaims(token.substring(7)).get(NAME);
        return name == null ? null : name.toString();
    }

    public Claims parseClaims(String token) {
        try {
            int lastIndexOfDot = token.lastIndexOf(StringUtil.DOT);
            return (Claims) Jwts.parser()
                    .parse(token.substring(0, lastIndexOfDot + 1))
                    .getBody();
        } catch (Exception ex) {
            log.warn("Expired token " + ex.getMessage());
        }

        return null;
    }

    private UserPrincipal getUserPrincipalFromClaims(Claims claims) {
        UserPrincipal userPrincipal = new UserPrincipal();

        UserRepresentation userRepresentation = new UserRepresentation();

        Object username = claims.get(PREFERRED_USERNAME);
        userRepresentation.setUsername(username == null ? null : username.toString());

        userRepresentation.setEmailVerified((Boolean) claims.get(EMAIL_VERIFIED));
        userRepresentation.setId((String) claims.get(SUB));

        userRepresentation.setEmail((String) claims.get(EMAIL));
        userRepresentation.setFirstName((String) claims.get(FAMILY_NAME));

        userRepresentation.setLastName((String) claims.get(GIVEN_NAME));
        Map<String, Object> realmAccesses = (Map<String, Object>) claims.get(KeycloakRealm.REALM_ACCESS);

        Set<GrantedAuthority> authorities;
        if (realmAccesses == null) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(ROLE_ANONYMOUS);
            authorities = new HashSet<>(Collections.singletonList(simpleGrantedAuthority));
        } else {
            authorities = ((List<String>) realmAccesses.get(ROLES))
                    .stream()
                    .map(roleName -> ROLE_PREFIX + roleName)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        }

        userPrincipal.setUserRepresentation(userRepresentation);
        userPrincipal.setAuthorities(authorities);

        return userPrincipal;
    }

    private void validateToken(String token) {
        if (StringUtils.hasText(token) || !token.startsWith(BEARER)) {
            throw new BusinessException(ResponseCode.INVALID_TOKEN);
        }
    }
}

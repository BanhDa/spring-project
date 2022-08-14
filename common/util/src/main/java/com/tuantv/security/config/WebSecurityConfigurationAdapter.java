package com.tuantv.security.config;

import com.tuantv.security.dto.PathMatcher;
import com.tuantv.security.jwt.JwtAuthenticationConverter;
import com.tuantv.security.jwt.KeycloakRoleConverter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@AllArgsConstructor
public class WebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

    private final SecurityConfigurationProperties securityProperties;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        configureAuthorizeRequests(httpSecurity);
        httpSecurity.exceptionHandling().authenticationEntryPoint(new ApplicationAuthenticationEntryPoint());

        // thêm converter để lấy role trong jwt token
        httpSecurity.oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());

    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtCollectionConverter(new KeycloakRoleConverter());

        return converter;
    }

    private HttpSecurity configureAuthorizeRequests(HttpSecurity httpSecurity) {
        AtomicReference<ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry> authorizeRequests
                = new AtomicReference<>();

        applyMatcherConfig(
            httpMethod -> authorizeRequests.set(authorizeRequests.get().antMatchers(httpMethod).permitAll()),
                paths -> authorizeRequests.set(authorizeRequests.get().antMatchers(paths.toArray(new String[0])).permitAll()),
                (method, paths) -> authorizeRequests.set(authorizeRequests.get().antMatchers(method, paths.toArray(new String[0])).permitAll())
        );

        return authorizeRequests.get()
                .anyRequest().authenticated()
                .and();
    }

    private void applyMatcherConfig(
            Consumer<HttpMethod> applyPermitAllMethods,
            Consumer<List<String>> applyPermitAllPaths,
            BiConsumer<HttpMethod, List<String>> applyMethodWithPaths
    ) {
        PathMatcher pathMatcher = securityProperties.getPathMatcher();
        if (pathMatcher == null) {
            return;
        }

        if (pathMatcher.isConfigHttpMethod()) {
            pathMatcher.getPermitAllMethods().forEach(applyPermitAllMethods);
        }

        if (pathMatcher.isConfigPermitAllPathPattern()) {
            applyPermitAllPaths.accept(pathMatcher.listPermitAllPathPatterns());
        }

        if (pathMatcher.isConfigPermitAllMap()) {
            pathMatcher.getPermitAllMap().forEach((httpMethod, patterns) -> {
                applyMethodWithPaths.accept(httpMethod,
                        patterns.stream().filter(Objects::nonNull).collect(Collectors.toList()));
            });
        }
    };
}

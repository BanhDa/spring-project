package com.tuantv.security.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class JwtConfiguration {

    @Value("${security.keycloak-realms.user-info-uri}")
    private String verifyToken;

}

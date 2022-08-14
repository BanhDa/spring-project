package com.tuantv.security.config;

import com.tuantv.security.dto.Cors;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@AllArgsConstructor
public class CorsConfig {

    private final SecurityConfigurationProperties securityProperties;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = buildCorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    private CorsConfiguration buildCorsConfiguration() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowCredentials(true);

        Cors cors = securityProperties.getCors();
        if (cors == null) {
            return corsConfiguration;
        }

        if (cors.getAllowedHeaders() != null) {
            corsConfiguration.setAllowedHeaders(cors.getAllowedHeaders());
        }

        if (cors.getAllowedOrigins() != null) {
            corsConfiguration.setAllowedOrigins(cors.getAllowedOrigins());
        }

        if (cors.getAllowedMethods() != null) {
            corsConfiguration.setAllowedHeaders(cors.getAllowedMethods());
        }

        return  corsConfiguration;
    }

}

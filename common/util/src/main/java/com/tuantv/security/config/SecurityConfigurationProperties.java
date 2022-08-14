package com.tuantv.security.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuantv.security.dto.Cors;
import com.tuantv.security.dto.PathMatcher;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "security", ignoreUnknownFields = false)
public class SecurityConfigurationProperties {

    @JsonProperty("cors")
    private Cors cors;

    /**
     * configure url path which are all allowed without authentication
     */
    @JsonProperty("path-matcher")
    private PathMatcher pathMatcher;
}

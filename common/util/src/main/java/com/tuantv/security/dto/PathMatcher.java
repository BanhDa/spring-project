package com.tuantv.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.http.HttpMethod;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class PathMatcher {

    @JsonProperty("permit-all-methods")
    private Set<HttpMethod> permitAllMethods;

    @JsonProperty("permit-all-path-patterns")
    private Set<String> permitAllPathPatterns;

    @JsonProperty("permit-all-map")
    private Map<HttpMethod, Set<String>> permitAllMap;

    public boolean isConfigHttpMethod() {
        return permitAllMethods != null;
    }

    public boolean isConfigPermitAllPathPattern() {
        return permitAllPathPatterns != null;
    }

    public List<String> listPermitAllPathPatterns() {
        if (permitAllPathPatterns == null) {
            return new ArrayList<>();
        }

        return permitAllPathPatterns.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public boolean isConfigPermitAllMap() {
        return permitAllMap != null;
    }
}

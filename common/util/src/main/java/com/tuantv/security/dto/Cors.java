package com.tuantv.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Cors {

    @JsonProperty("allowed-origins")
    private List<String> allowedOrigins;

    @JsonProperty("allowed-methods")
    private List<String> allowedMethods;

    @JsonProperty("allowed-headers")
    private List<String> allowedHeaders;

}

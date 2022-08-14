package com.tuantv.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessException extends RuntimeException {

    /**
     * Value of response code
     */
    private String responseCode;

    /**
     * data
     */
    private Object data;

    /**
     * Data append to error message
     */
    private List<Object> messageParams;

    /**
     * Describe context of exception
     */
    private String context;

    public BusinessException(String responseCode) {
        this.responseCode = responseCode;
    }
}

package com.tuantv.dto.response;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseCode {

    public static final String COMMON_RESPONSE_CODE_PREFIX = "COM_";

    /**
     * UNAUTHORIZED
     */
    public static final String INVALID_TOKEN = COMMON_RESPONSE_CODE_PREFIX + "4010002";


}

package com.tuantv.cqrs;

import com.tuantv.dto.request.RequestData;
import com.tuantv.dto.response.ResponseData;

public class UnsupportedHandler implements Handler<RequestData, ResponseData> {
    public ResponseData handle(RequestData requestData) {
        return null;
    }
}

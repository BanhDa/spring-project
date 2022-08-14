package com.tuantv.cqrs;

import com.tuantv.dto.request.RequestData;
import com.tuantv.dto.response.ResponseData;

public interface Handler <T extends RequestData, I extends ResponseData>{

    I handle(T requestData);

}

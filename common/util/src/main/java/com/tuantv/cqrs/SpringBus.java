package com.tuantv.cqrs;

import com.tuantv.dto.request.RequestData;
import com.tuantv.dto.response.ResponseData;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SpringBus implements CqrsBus {

    private final Registry registry;

    public <T extends RequestData, I extends ResponseData> I execute(T requestData) {
        Handler handler = registry.getHandler(requestData);
        return (I) handler.handle(requestData);
    }
}

package com.tuantv.cqrs.query;

import com.tuantv.cqrs.Handler;
import com.tuantv.dto.request.QueryRequestData;
import com.tuantv.dto.response.QueryResponseData;

public abstract class QueryHandler<T extends QueryRequestData, I extends QueryResponseData> implements Handler<T, I> {
}

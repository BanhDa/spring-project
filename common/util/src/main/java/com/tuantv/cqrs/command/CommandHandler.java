package com.tuantv.cqrs.command;

import com.tuantv.cqrs.Handler;
import com.tuantv.dto.request.CommandRequestData;
import com.tuantv.dto.response.CommandResponseData;

public abstract class CommandHandler<T extends CommandRequestData, I extends CommandResponseData> implements Handler<T, I> {
}

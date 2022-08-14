package com.tuantv.cqrs;

import com.tuantv.cqrs.command.CommandHandler;
import com.tuantv.cqrs.query.QueryHandler;
import com.tuantv.dto.request.CommandRequestData;
import com.tuantv.dto.request.QueryRequestData;
import com.tuantv.dto.request.RequestData;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Registry {

    private static final UnsupportedHandler UNSUPPORTED_HANDLER = new UnsupportedHandler();

    private static final Map<Class<? extends QueryRequestData>, QueryHandler> QUERY_HANDLER_CONTAINER
            = new HashMap<Class<? extends QueryRequestData>, QueryHandler>();

    private static final Map<Class<? extends CommandRequestData>, CommandHandler> COMMAND_HANDLER_CONTAINER
            = new HashMap<Class<? extends CommandRequestData>, CommandHandler>();

    private final ApplicationContext applicationContext;

    public Registry(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        initQueryHandlerBeans();
        initCommandHandlerBeans();
    }

    public <R extends RequestData> Handler getHandler(R request) {
        if (request == null) {
            return UNSUPPORTED_HANDLER;
        }
        Handler handler;
        if (request instanceof CommandRequestData) {
            handler = COMMAND_HANDLER_CONTAINER.get(request.getClass());
        } else {
            handler = QUERY_HANDLER_CONTAINER.get(request.getClass());
        }

        return handler == null ? UNSUPPORTED_HANDLER : handler;
    }

    private void initQueryHandlerBeans() {
        String[] queryHandlerBeanNames = applicationContext.getBeanNamesForType(QueryHandler.class);
        for (String beanName : queryHandlerBeanNames) {
            initQueryHandlerBean(beanName);
        }
    }

    private void initQueryHandlerBean(String queryHandlerBeanName) {
        Class<QueryHandler<?, ?>> handlerClassType = (Class<QueryHandler<?, ?>>) applicationContext.getType(queryHandlerBeanName);
        Class<?>[] argumentClassTypes = GenericTypeResolver.resolveTypeArguments(handlerClassType, QueryHandler.class);
        QUERY_HANDLER_CONTAINER.put((Class<? extends QueryRequestData>) argumentClassTypes[0], (QueryHandler) applicationContext.getBean(queryHandlerBeanName));
    }

    private void initCommandHandlerBeans() {
        String[] commandHandlerBeanNames = applicationContext.getBeanNamesForType(CommandHandler.class);
        for (String commandHandlerBeanName : commandHandlerBeanNames) {
            initCommandBean(commandHandlerBeanName);
        }

    }

    private void initCommandBean(String commandHandlerBeanName) {
        Class<CommandHandler<?, ?>> commandHandlerClassTypes = (Class<CommandHandler<?, ?>>) applicationContext.getType(commandHandlerBeanName);
        Class<?>[] argumentClassTypes = GenericTypeResolver.resolveTypeArguments(commandHandlerClassTypes, CommandHandler.class);
        COMMAND_HANDLER_CONTAINER.put((Class<? extends CommandRequestData>) argumentClassTypes[0], (CommandHandler) applicationContext.getBean(commandHandlerBeanName));
    }
}

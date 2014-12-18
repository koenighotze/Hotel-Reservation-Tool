package org.koenighotze.jee7hotel.business.eventsource;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.logging.Logger;

/**
 * Created by dschmitz on 27.11.14.
 */

@Interceptor
@EventSource
public class EventSourceInterceptor {
    private static final Logger LOGGER = Logger.getLogger(EventSourceInterceptor.class.getName());

    @Inject
    private IEventSource eventSourceBean;

    @AroundInvoke
    public Object around(InvocationContext ctx) throws Exception {
        LOGGER.info(() -> "Handling event source " + ctx.getMethod().getName());

        this.eventSourceBean.storeEvent(ctx.getTarget().getClass(), ctx.getMethod(), ctx.getParameters());

        return ctx.proceed();
    }
}

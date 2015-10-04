package org.koenighotze.jee7hotel.business;

import org.koenighotze.jee7hotel.framework.eventing.eventsource.IEventSource;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import static java.lang.String.format;

/**
 * Alternative event source.
 *
 * Off course a rather stupid example.
 *
 * @author dschmitz
 */
@Named

// override alternative from framework lib
@Alternative
@Priority(1)
public class GuestLoggingEventSource implements IEventSource {
    private static final Logger LOGGER = Logger.getLogger("GUEST_EVENTS");

    @Override
    public Object storeEvent(@NotNull Class<?> clazz, @NotNull Method method, Object[] parameters) {
        LOGGER.info(format("Special guest event: class=%s, method=%s", clazz.getName(), method.getName()));
        return null;
    }
}

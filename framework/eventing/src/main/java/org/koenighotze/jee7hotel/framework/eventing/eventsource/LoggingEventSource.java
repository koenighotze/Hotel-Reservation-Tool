package org.koenighotze.jee7hotel.framework.eventing.eventsource;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Logger;

import static java.lang.String.format;

/**
 * Alternative event source.
 *
 * Must be activated using beans.xml declaration.
 *
 * @author dschmitz
 */
@Named
@Priority(0)
@Alternative
public class LoggingEventSource implements IEventSource {
    private static final Logger LOGGER = Logger.getLogger("EVENT_LOG");

    @Override
    public Object storeEvent(@NotNull Class<?> clazz, @NotNull Method method, Object[] parameters) {
        LOGGER.info(() -> format("Storing event: class=%s, method=%s, params=%s", clazz.getName(), method.getName(), Arrays.toString(parameters)));
        return null;
    }
}

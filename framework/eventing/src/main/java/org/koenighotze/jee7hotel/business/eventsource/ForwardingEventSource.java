package org.koenighotze.jee7hotel.business.eventsource;

import javax.enterprise.inject.Alternative;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Logger;

import static java.lang.String.format;

/**
 * @author dschmitz
 */
@Singleton
@Alternative
public class ForwardingEventSource implements IEventSource {
    private static final Logger LOGGER = Logger.getLogger(ForwardingEventSource.class.getName());
    @Override
    public Object storeEvent(@NotNull Class<?> clazz, @NotNull Method method, Object[] parameters) {
        LOGGER.info(() -> format("Storing event: class=%s, method=%s, params=%s", clazz.getName(), method.getName(), Arrays.toString(parameters)));
        return null;
    }
}

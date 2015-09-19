package org.koenighotze.jee7hotel.business.eventsource;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;

/**
 * @author koenighotze
 */
public interface IEventSource {
    // TODO Versioning, Typeinfo, id, synchronization
    Object storeEvent(@NotNull Class<?> clazz, @NotNull Method method, Object[] parameters);
}

package org.koenighotze.jee7hotel.business.eventsource;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;

/**
 * Created by dschmitz on 28.11.14.
 */
public interface IEventSource {
    // TODO Versioning, Typeinfo, id, synchronization
    Object storeEvent(@NotNull Class<?> clazz, @NotNull Method method, Object[] parameters);
}

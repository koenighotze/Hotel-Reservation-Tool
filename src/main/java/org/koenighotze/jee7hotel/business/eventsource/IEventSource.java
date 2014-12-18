package org.koenighotze.jee7hotel.business.eventsource;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by dschmitz on 28.11.14.
 */
public interface IEventSource {
    // TODO: warum muss ich das so machen??? Warum geht List<Event> nicht?
    @GET
    @Produces({"application/xml", "application/json"})
    List<Event> getAll();

    List<Event> fetchAllEvents();

    // TODO Versioning, Typeinfo, id, synchronization
    Object storeEvent(@NotNull Class<?> clazz, @NotNull Method method, Object[] parameters);

    default void clearAll() {
        // ..no op
    };
}

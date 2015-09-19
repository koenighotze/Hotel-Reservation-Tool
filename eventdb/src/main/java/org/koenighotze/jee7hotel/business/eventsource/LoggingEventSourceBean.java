package org.koenighotze.jee7hotel.business.eventsource;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author koenighotze
 */
//@Singleton
//@Alternative
//@Priority(Interceptor.Priority.APPLICATION + 10)
public class LoggingEventSourceBean implements IEventSource{
    private static final Logger LOGGER = Logger.getLogger(LoggingEventSourceBean.class.getName());

    private Queue<Event> queue = new LinkedBlockingQueue<>(200);

    public List<Event> getAll() {
        return new LinkedList<>(this.queue); // .stream().map(e -> e.toString()).collect(Collectors.toList());
    }

    public List<Event> fetchAllEvents() {
        return this.queue.stream().collect(Collectors.toList());
    }

    @Override
    public Object storeEvent(@NotNull Class<?> clazz, @NotNull Method method, Object[] parameters) {
        Event event = new Event(clazz.getName(), method.getName(), Instant.now().toEpochMilli(), parameters);

        LOGGER.info(() -> "Storing event " + event);

        while (!this.queue.offer(event)) {
            this.queue.poll();
        }

        return event.hashCode();
    }
}

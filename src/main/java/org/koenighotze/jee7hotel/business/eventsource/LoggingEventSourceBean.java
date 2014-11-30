package org.koenighotze.jee7hotel.business.eventsource;

import javax.annotation.Priority;
import javax.ejb.Singleton;
import javax.enterprise.inject.Alternative;
import javax.interceptor.Interceptor;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by dschmitz on 28.11.14.
 */
@Singleton
@Alternative
@Priority(Interceptor.Priority.APPLICATION + 1)
public class LoggingEventSourceBean implements IEventSource {
    private static final Logger LOGGER = Logger.getLogger(LoggingEventSourceBean.class.getName());

    private Queue<Event> queue = new LinkedBlockingQueue<>(200);

    @Override
    public List<String> getAll() {
        return this.queue.stream().map(e -> e.toString()).collect(Collectors.toList());
    }

    @Override
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
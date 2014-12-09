package org.koenighotze.jee7hotel.business.eventsource;

import com.mongodb.*;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This bean is an example of a naive implementation of event sourcing.
 *
 * Basically what happens is: this bean observes all events and stores them in a
 * mongo db.
 *
 * Todo: replay ability; export via Atom or such
 *
 * Created by dschmitz on 26.11.14.
 */

@Stateless
//@Path("events")
public class EventSourceBean implements IEventSource {

    private static final Logger LOGGER = Logger.getLogger(EventSourceBean.class.getName());

    private MongoClient mongoClient;

    public void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @PostConstruct
    public void init() {
        // TODO configure me
        try {
            LOGGER.info("Creating mongo connection");
            this.mongoClient = new MongoClient();
            LOGGER.info("Connected to " + this.mongoClient);
        } catch (UnknownHostException e) {
            // must not throw checked ex.
            throw new ExceptionInInitializerError(e);
        }
    }

    public void close() {
        LOGGER.info("Closing mongo connection");
        this.mongoClient.close();
    }

    // TODO: warum muss ich das so machen???
    @Override
    @GET
    @Produces({"application/xml", "application/json"})
    public List<String> getAll() {
        return fetchAllEvents().stream().map(e -> e.toString()).collect(Collectors.toList());
    }

    @Override
    public List<Event> fetchAllEvents() {
        DB db = mongoClient.getDB("jee7hotel");
        DBCollection coll = db.getCollection("events");

        List<Event> result = new ArrayList<>();

        try (DBCursor cursor = coll.find()) {
            while (cursor.hasNext()) {
                DBObject next = cursor.next();
                LOGGER.info("Parsing " + next);
                result.add(Event.fromJson((String) next.get("event")));
            }
        }

        LOGGER.info("Returning " + result);
        return result;
    }

    // TODO Versioning, Typeinfo, id, synchronization
    @Override
    public Object storeEvent(@NotNull Class<?> clazz, @NotNull Method method, Object[] parameters) {
        LOGGER.fine(() -> "Storing event " + clazz.getName() + "#" + method.getName());

        DB db = this.mongoClient.getDB("jee7hotel");
        DBCollection coll = db.getCollection("events");

        Event event = new Event(clazz.getName(), method.getName(), Instant.now().toEpochMilli(), parameters);
        BasicDBObject obj = new BasicDBObject("event", event.toJson()); // TODO: introduce event description name

        LOGGER.fine(() -> "Adding event" + obj);
        WriteResult writeResult = coll.insert(obj);

        return writeResult.getUpsertedId();
    }
}

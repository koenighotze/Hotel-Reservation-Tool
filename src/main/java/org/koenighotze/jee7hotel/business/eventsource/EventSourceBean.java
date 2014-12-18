package org.koenighotze.jee7hotel.business.eventsource;

import com.mongodb.*;
import org.mongojack.*;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;
import javax.interceptor.Interceptor;
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

/**
 * This bean is an example of a naive implementation of event sourcing.
 *
 * Basically what happens is: this bean observes all events and stores them in a
 * mongo db.
 *
 * Created by dschmitz on 26.11.14.
 */

@Singleton
@Path("events")
@Alternative
@Priority(Interceptor.Priority.APPLICATION + 9)
public class EventSourceBean implements IEventSource {

    private static final Logger LOGGER = Logger.getLogger(EventSourceBean.class.getName());

    private MongoClient mongoClient;

    private String dbName = "jee7hotel";

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

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

    @Override
    @GET
    @Produces({"application/xml", "application/json"})
    public List<Event> getAll() {
        return fetchAllEvents();
    }

    @Override
    public void clearAll() {
        LOGGER.warning("Dropping collection events in database " + this.dbName);
        DB db = mongoClient.getDB(this.dbName);
        DBCollection coll = db.getCollection("events");
        coll.drop();
    }

    @Override
    public List<Event> fetchAllEvents() {
        DB db = mongoClient.getDB(this.dbName);
        DBCollection coll = db.getCollection("events");
        JacksonDBCollection<Event, Object> wrap = JacksonDBCollection.wrap(coll, Event.class);

        List<Event> result = new ArrayList<>();
        org.mongojack.DBCursor<Event> cursor = null;
        try {
            cursor = wrap.find();
            while (cursor.hasNext()) {
                Event next = cursor.next();
                LOGGER.info("Found " + next);
                result.add(next);
            }
        }
        finally {
            if (null != cursor) {
                cursor.close();
            }
        }

        LOGGER.info("Returning " + result);
        return result;
    }

    @Override
    public Object storeEvent(@NotNull Class<?> clazz, @NotNull Method method, Object[] parameters) {
        LOGGER.fine(() -> "Storing event " + clazz.getName() + "#" + method.getName());

        DB db = this.mongoClient.getDB(this.dbName);
        DBCollection coll = db.getCollection("events");

        JacksonDBCollection<Event, Object> wrap = JacksonDBCollection.wrap(coll, Event.class);
        Event event = new Event(clazz.getName(), method.getName(), Instant.now().toEpochMilli(), parameters);
        org.mongojack.WriteResult<Event, Object> result = wrap.insert(event);
        LOGGER.info("Stored event under id " + result.getSavedId());
        return result.getSavedId();
    }
}

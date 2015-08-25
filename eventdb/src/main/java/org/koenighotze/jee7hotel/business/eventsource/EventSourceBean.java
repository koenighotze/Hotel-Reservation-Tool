package org.koenighotze.jee7hotel.business.eventsource;

import com.github.fakemongo.Fongo;
import com.mongodb.*;
import org.mongojack.JacksonDBCollection;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Priority;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Alternative;
import javax.interceptor.Interceptor;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Collections.singletonList;

/**
 * This bean is an example of a naive implementation of event sourcing.
 * <p>
 * Basically what happens is: this bean observes all events and stores them in a
 * mongo db.
 * <p>
 * Created by dschmitz on 26.11.14.
 */

@Singleton
@Path("events")
@Alternative
@Priority(Interceptor.Priority.APPLICATION + 9)
@Startup
public class EventSourceBean implements IEventSource {

    private static final Logger LOGGER = Logger.getLogger(EventSourceBean.class.getName());

    private MongoClient mongoClient;

    private String dbName = "jee7hotel";

    private Fongo fongo;

    @PostConstruct
    public void init() {
        try {
            LOGGER.info("Creating mongo connection");
            MongoClientOptions options = MongoClientOptions.builder().connectTimeout(1000).build();

            String mongoHost = System.getProperty("mongo.host", "localhost");
            String mongoPort = System.getProperty("mongo.port", "27017");

            LOGGER.info(() -> "Connecting to " + mongoHost + ":" + mongoPort);

            this.mongoClient = new MongoClient(
                    singletonList(
                            new ServerAddress(mongoHost,
                                    Integer.parseInt(mongoPort))),
                    options);
            this.mongoClient.getDatabaseNames(); // force init
            LOGGER.info("Connected to " + this.mongoClient);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e, () -> "Cannot connect to MongoDB...starting internal fongo instance");
            this.fongo = new Fongo("fongoServer1");
            this.mongoClient = fongo.getMongo();
        }
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @PreDestroy
    public void close() {
        LOGGER.info("Closing mongo connection");
        this.mongoClient.close();
        this.fongo = null;
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<Event> getAll() {
        return fetchAllEvents();
    }

    public void clearAll() {
        LOGGER.warning("Dropping collection events in database " + this.dbName);
        DB db = mongoClient.getDB(this.dbName);
        DBCollection coll = db.getCollection("events");
        coll.drop();
    }

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
        } finally {
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

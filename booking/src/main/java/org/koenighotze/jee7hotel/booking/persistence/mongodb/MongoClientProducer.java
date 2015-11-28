package org.koenighotze.jee7hotel.booking.persistence.mongodb;

import com.github.fakemongo.Fongo;
import com.mongodb.MongoClient;
import org.bson.Document;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.util.ArrayList;
import java.util.logging.Logger;

import static com.mongodb.MongoClientOptions.builder;
import static java.util.stream.Collectors.joining;

/**
 * Producer for MongoDB Clients.
 *
 * Falls back to using an in memory FongoDB version of MongoDB
 *
 * @author dschmitz
 */
@ApplicationScoped
public class MongoClientProducer {
    private static final Logger LOGGER = Logger.getLogger(MongoClientProducer.class.getName());

    private MongoClient mongoClient;

    @PostConstruct
    public void setup() {
        try {
            LOGGER.info(() -> "Starting MongoClient");
            mongoClient = new MongoClient("mongodb", builder().connectTimeout(1000).serverSelectionTimeout(3000).socketTimeout(1000).build());
            LOGGER.info(() ->  {
                String dbs = mongoClient.listDatabases().map(Document::toJson).into(new ArrayList<>()).stream().collect(joining(", "));
                return "Connected to MongoDB, known Databases: " + dbs;
            });
        }
        catch (Exception e) {
            LOGGER.warning(() -> "Cannot startup MongoClient..falling back to fake mode");
            mongoClient = new Fongo("inmemo").getMongo();
        }

    }
    @PreDestroy
    public void cleanUp() {
        LOGGER.info(() -> "Closing MongoClient");
        mongoClient.close();
    }

    @Produces
    public MongoClient mongoClient() {
        return mongoClient;
    }

}

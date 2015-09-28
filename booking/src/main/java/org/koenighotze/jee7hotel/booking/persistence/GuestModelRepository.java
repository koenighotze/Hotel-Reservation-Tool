package org.koenighotze.jee7hotel.booking.persistence;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.koenighotze.jee7hotel.booking.domain.GuestModel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;
import static java.util.Optional.empty;

/**
 * A mongodb based repository for guest models, i.e. a local version
 * of guest data.
 *
 * @author dschmitz
 */
@Named
@ApplicationScoped
public class GuestModelRepository {
    private MongoClient mongoClient;

    public GuestModelRepository() {
    }

    @Inject
    public GuestModelRepository(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public void storeGuestModel(@NotNull GuestModel guestModel) {
        MongoDatabase db = mongoClient.getDatabase("booking");
        MongoCollection<Document> coll = db.getCollection("guestmodel");

        if (null != coll.find(eq("publicId", guestModel.getPublicId())).limit(1).first()) {
            throw new RuntimeException("Guest with " + guestModel.getPublicId() + " already exists!");
        }

        // obviously crap, but mongojack does not seem to support Mongo3?
        Document doc = new Document("name", guestModel.getName())
                .append("publicId", guestModel.getPublicId());
        coll.insertOne(doc);
    }

    public Optional<GuestModel> findByPublicId(@NotNull String publicId) {
        MongoDatabase db = mongoClient.getDatabase("booking");
        MongoCollection<Document> coll = db.getCollection("guestmodel");
        Document doc = coll.find(eq("publicId", publicId)).first();

        if (null == doc) {
            return empty();
        }

        return Optional.of(new GuestModel(doc.getString("publicId"), doc.getString("name")));
    }
}

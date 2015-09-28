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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

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
        MongoCollection<Document> coll = getGuestModelCollection();

        if (null != coll.find(eq("publicId", guestModel.getPublicId())).limit(1).first()) {
            throw new RuntimeException("Guest with " + guestModel.getPublicId() + " already exists!");
        }

        // obviously crap, but mongojack does not seem to support Mongo3?
        Document doc = new Document("name", guestModel.getName())
                .append("publicId", guestModel.getPublicId());
        coll.insertOne(doc);
    }

    public Optional<GuestModel> findByPublicId(@NotNull String publicId) {
        MongoCollection<Document> coll = getGuestModelCollection();
        Document doc = coll.find(eq("publicId", publicId)).first();

        if (null == doc) {
            return empty();
        }

        return Optional.of(createGuestModelFromDocument(doc));
    }

    protected GuestModel createGuestModelFromDocument(Document doc) {
        return new GuestModel(doc.getString("publicId"), doc.getString("name"));
    }

    protected MongoCollection<Document> getGuestModelCollection() {
        return getBookingDb().getCollection("guestmodel");
    }

    protected MongoDatabase getBookingDb() {
        return mongoClient.getDatabase("booking");
    }

    public List<GuestModel> findAllGuests() {
        MongoCollection<Document> guestModelCollection = getGuestModelCollection();

        List<GuestModel> result = new ArrayList<>();
        guestModelCollection.find().forEach((Consumer<Document>) document -> {
            result.add(createGuestModelFromDocument(document));
        });
        return result;
    }
}

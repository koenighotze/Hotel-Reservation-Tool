package org.koenighotze.jee7hotel.booking.domain;

import com.github.fakemongo.Fongo;
import com.mongodb.MongoClient;
import org.junit.Test;
import org.koenighotze.jee7hotel.booking.persistence.GuestModelRepository;

import java.util.Optional;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author dschmitz
 */
public class GuestModelRepositoryTest {

    private MongoClient mongoClient = new Fongo("foo").getMongo();

    @Test
    public void a_guest_model_can_be_stored() {
        GuestModelRepository repository = new GuestModelRepository(mongoClient);
        GuestModel model = new GuestModel("", "");
        repository.storeGuestModel(model);
    }

    @Test
    public void unknown_guests_cannot_result_in_an_empty_optional() {
        GuestModelRepository repository = new GuestModelRepository(mongoClient);
        Optional<GuestModel> opt = repository.findByPublicId("foo");

        assertThat(opt.isPresent()).isFalse();
    }

    @Test
    public void a_stored_guest_can_be_retrieved() {
        GuestModelRepository repository = new GuestModelRepository(mongoClient);
        GuestModel model = new GuestModel("foo", "bar");
        repository.storeGuestModel(model);

        GuestModel found = repository.findByPublicId("foo").get();
        assertThat(found).isEqualTo(model);
    }

    @Test(expected = RuntimeException.class) // replace with real exception
    public void storing_a_guest_with_an_existing_publicid_fails() {
        GuestModelRepository repository = new GuestModelRepository(mongoClient);
        GuestModel model = new GuestModel("foo", "bar");
        repository.storeGuestModel(model);

        repository.storeGuestModel(model);
    }


    @Test
    public void find_all_returns_empty_collection_if_db_is_empty() {
        GuestModelRepository repository = new GuestModelRepository(mongoClient);
        assertThat(repository.findAllGuests()).isEmpty();
    }


    @Test
    public void find_all_returns_all_stored_guests() {
        GuestModelRepository repository = new GuestModelRepository(mongoClient);
        GuestModel model = new GuestModel("foo", "bar");
        repository.storeGuestModel(model);

        assertThat(repository.findAllGuests()).contains(model);
    }



}
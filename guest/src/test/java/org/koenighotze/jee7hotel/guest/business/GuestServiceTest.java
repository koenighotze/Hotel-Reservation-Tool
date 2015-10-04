

package org.koenighotze.jee7hotel.guest.business;

import org.junit.Test;
import org.koenighotze.jee7hotel.framework.test.AbstractBasePersistenceTest;
import org.koenighotze.jee7hotel.guest.domain.Guest;

import javax.enterprise.event.Event;
import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * @author dschmitz
 */
public class GuestServiceTest extends AbstractBasePersistenceTest {
    private GuestService guestService;

    private static final Long WELL_KNOWN_ID = 9999L;

    @Override
    @SuppressWarnings("unchecked")
    protected void initHook() {
        guestService = new GuestService(mock(Event.class));
        guestService.setEntityManager(getEntityManager());
    }

    @Override
    protected void cleanupHook() {
        guestService = null;
    }

    @Test
    public void finding_all_guests_does_not_return_null() {
        assertThat(guestService.getAllGuests()).isNotNull();
    }

    @Test
    public void saving_a_guest_sets_the_technical_id() {
        Guest guest = new Guest("jjjkl", "name", "email@foo.de");

        guestService.saveGuest(guest);
        getEntityManager().flush(); // fore id pickup

        assertThat(guest.getId()).isNotNull();
    }

    @Test
    public void a_known_guest_can_be_found_by_his_public_id() {
        final String PUBLIC_ID = "jjjkl";
        Guest guest = new Guest(PUBLIC_ID, "name", "email@foo.de");

        guestService.saveGuest(guest);
        getEntityManager().flush();

        Optional<Guest> result = guestService.findByPublicId(PUBLIC_ID);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(guest.getId());
    }

    @Test
    public void an_unkown_guest_results_in_an_empty_optional() {
        Optional<Guest> result = guestService.findByPublicId(randomUUID().toString());

        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void updating_a_guest_sets_all_fields_and_increases_the_version() {
        final String newName = "Bratislav Metulski";

        Optional<Guest> guest = guestService.findById(WELL_KNOWN_ID);
        assertThat(guest.isPresent()).isTrue();
        assertThat(guest.get().getName()).isNotEqualTo(newName);

        guest.get().setName(newName);
        getEntityManager().detach(guest.get());

        Optional<Guest> updated = guestService.updateGuestDetails(guest.get());
        getEntityManager().flush();

        assertThat(guest.get().getName()).isEqualTo(newName);

        assertThat(updated.get().getVersion()).isGreaterThan(guest.get().getVersion());
    }
}

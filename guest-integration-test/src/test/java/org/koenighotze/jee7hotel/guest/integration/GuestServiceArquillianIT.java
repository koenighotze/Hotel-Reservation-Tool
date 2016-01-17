package org.koenighotze.jee7hotel.guest.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.jee7hotel.guest.business.GuestService;
import org.koenighotze.jee7hotel.guest.domain.Guest;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.koenighotze.jee7hotel.framework.integration.BaseArquillianSetup.createStandardDeployment;

/**
 * @author dschmitz
 */
@RunWith(Arquillian.class)
public class GuestServiceArquillianIT {

    @Inject
    private GuestService guestService;

    @Deployment
    public static Archive<?> createMicroDeployment() {
        return createStandardDeployment(GuestServiceArquillianIT.class.getPackage());
    }

    @Test
    public void a_stored_guest_can_be_loaded_via_the_primary_key() {
        Guest guest = new Guest("foo", "name", "email");
        guestService.saveGuest(guest);

        assertNotNull(guest.getId());

        Guest loaded = guestService.findById(guest.getId()).get();
        assertEquals(loaded.getId(), guest.getId());
        assertEquals(loaded.getName(), guest.getName());
        assertEquals(loaded.getEmail(), guest.getEmail());
    }

    @Test
    public void creating_a_guest_sends_an_appropriate_message() {
        // todo
    }
}



package org.koenighotze.jee7hotel.framework.test.guest.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.jee7hotel.guest.business.GuestService;
import org.koenighotze.jee7hotel.guest.domain.Guest;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.net.URL;
import java.util.Arrays;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.koenighotze.jee7hotel.framework.integration.BaseArquillianSetup.createStandardDeployment;

/**
 * @author dschmitz
 */
@RunWith(Arquillian.class)
public class GuestServiceArquillianIntegrationIT {
    private static final Logger LOGGER = Logger.getLogger(GuestServiceArquillianIntegrationIT.class.getName());

    @Inject
    private GuestService guestService;

    @Deployment
    public static Archive<?> createMicroDeployment() {
        return createStandardDeployment(GuestServiceArquillianIntegrationIT.class.getPackage());
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

    @Test
    @RunAsClient
    public void guests_can_be_fetched_via_the_rest_api(@ArquillianResource URL baseURI) throws InterruptedException {
        String targetUrl = baseURI + "rest/guest";

        LOGGER.info("Looking up rest interface using " + targetUrl);

        // Client does not implement autoclosable :(
        Client client = null;
        WebTarget webTarget = null;

        try {
            client = ClientBuilder.newClient();
            webTarget = client.target(targetUrl);

            Guest[] guests = webTarget
                    .request()
                    .get(Guest[].class);

            LOGGER.info(() -> "Received " + Arrays.toString(guests));
//            assertThat("expected an array of guests", guests, is(not(nullValue())));
//            assertThat("expected one or more guests", guests.length, is(not(equalTo(0))));
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }
}



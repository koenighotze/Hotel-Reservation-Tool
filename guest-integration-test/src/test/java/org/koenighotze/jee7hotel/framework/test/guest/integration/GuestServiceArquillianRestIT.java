package org.koenighotze.jee7hotel.framework.test.guest.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.jee7hotel.guest.domain.Guest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.net.URL;
import java.util.Arrays;
import java.util.logging.Logger;

import static org.fest.assertions.Assertions.assertThat;
import static org.koenighotze.jee7hotel.framework.integration.BaseArquillianSetup.createStandardDeployment;

/**
 * @author dschmitz
 */
@RunWith(Arquillian.class)
public class GuestServiceArquillianRestIT {
    private static final Logger LOGGER = Logger.getLogger(GuestServiceArquillianRestIT.class.getName());
    private Client client;

    @Deployment
    public static Archive<?> createMicroDeployment() {
        return createStandardDeployment(GuestServiceArquillianIT.class.getPackage());
    }

    @Before
    public void setupWebClient() {
        client = ClientBuilder.newClient();
    }

    @After
    public void cleanupWebClient() {
        if (null != client) {
            client.close();
        }
    }

    @Test
    @RunAsClient
    public void a_known_guest_can_be_fetched_using_the_rest_service(@ArquillianResource URL baseURI) throws InterruptedException {
        String targetUrl = baseURI + "rest/guest/9999"; // well known guest id

        WebTarget webTarget = client.target(targetUrl);

        Guest guest = webTarget
                .request()
                .get(Guest.class);

        assertThat(guest).isNotNull();
        assertThat(guest.getPublicId()).isEqualTo("dschm1");
    }

    @Test
    @RunAsClient
    public void guests_can_be_fetched_via_the_rest_api(@ArquillianResource URL baseURI) throws InterruptedException {
        String targetUrl = baseURI + "rest/guest";

        LOGGER.fine("Looking up rest interface using " + targetUrl);

        WebTarget webTarget = client.target(targetUrl);

        Guest[] guests = webTarget
                .request()
                .get(Guest[].class);

        LOGGER.fine("Received " + Arrays.toString(guests));
        assertThat(guests.length).isGreaterThan(0);
    }
}

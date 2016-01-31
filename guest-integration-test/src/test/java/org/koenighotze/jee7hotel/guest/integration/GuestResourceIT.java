package org.koenighotze.jee7hotel.guest.integration;

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
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.net.URL;
import java.util.Arrays;
import java.util.logging.Logger;

import static java.util.UUID.randomUUID;
import static javax.ws.rs.client.ClientBuilder.newClient;
import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.HttpHeaders.LOCATION;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.*;
import static org.fest.assertions.Assertions.assertThat;
import static org.koenighotze.jee7hotel.framework.integration.BaseArquillianSetup.createStandardDeployment;

/**
 * @author dschmitz
 */
@RunWith(Arquillian.class)
public class GuestResourceIT {
    private static final Logger LOGGER = Logger.getLogger(GuestResourceIT.class.getName());
    private Client client;

    @Deployment
    public static Archive<?> createMicroDeployment() {
        return createStandardDeployment(GuestServiceArquillianIT.class.getPackage());
    }

    @Before
    public void setupWebClient() {
        client = newClient();
    }

    @After
    public void cleanupWebClient() {
        if (null != client) {
            client.close();
        }
    }

    @Test
    @RunAsClient
    public void a_guest_can_be_created_using_post(@ArquillianResource URL baseURI) {
        String targetUrl = baseURI + "rest/guests/";

        WebTarget webTarget = client.target(targetUrl);

        final Guest guest = new Guest("", "Bratislav", "metulski@foobar.com");
        Response response = webTarget
                .request(APPLICATION_JSON_TYPE)
                .post(json(guest));

        assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());
        final String location = response.getHeaderString(LOCATION);
        assertThat(location).isNotEmpty();

        Response getResponse = client.target(location).request(APPLICATION_JSON_TYPE).get();
        assertThat(getResponse.getStatus()).isEqualTo(OK.getStatusCode());

        final Guest createdGuest = getResponse.readEntity(Guest.class);
        assertThat(createdGuest).isNotNull();
        assertThat(location).endsWith(createdGuest.getPublicId());
        assertThat(guest.getEmail()).isEqualTo("metulski@foobar.com");
        assertThat(guest.getName()).isEqualTo("Bratislav");
    }

    @Test
    @RunAsClient
    public void unknown_guests_result_in_a_not_found(@ArquillianResource URL baseURI) {
        String targetUrl = baseURI + "rest/guests/notthere";

        Response response = client.target(targetUrl).request(APPLICATION_JSON_TYPE).get();
        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
    }

    @Test
    @RunAsClient
    public void guests_can_be_fetched_via_the_rest_api(@ArquillianResource URL baseURI) throws InterruptedException {
        String targetUrl = baseURI + "rest/guests";

        LOGGER.fine("Looking up rest interface using " + targetUrl);

        WebTarget webTarget = client.target(targetUrl);

        Guest[] guests = webTarget
                .request(APPLICATION_JSON_TYPE)
                .get(Guest[].class);

        LOGGER.fine("Received " + Arrays.toString(guests));
        assertThat(guests.length).isGreaterThan(0);
    }


    @Test
    @RunAsClient
    public void deleting_existing_guest(@ArquillianResource URL baseURI) throws InterruptedException {
        String targetUrl = baseURI + "rest/guests/";

        WebTarget webTarget = client.target(targetUrl);

        Response response = webTarget
                .request(APPLICATION_JSON_TYPE)
                .post(json(new Guest("", "Bratislav", "metulski@foobar.com")));

        assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());
        final String location = response.getHeaderString(LOCATION);

        final Guest guest = client.target(location).request(APPLICATION_JSON_TYPE).get(Guest.class);

        response = webTarget
                .path(guest.getPublicId())
                .request(APPLICATION_JSON_TYPE)
                .delete();

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
    }


    @Test
    @RunAsClient
    public void deleting_existing_unknown_guest_results_in_not_found(@ArquillianResource URL baseURI) throws InterruptedException {
        String targetUrl = baseURI + "rest/guests/";

        WebTarget webTarget = client.target(targetUrl);

        Response response = webTarget
                .path(randomUUID().toString())
                .request(APPLICATION_JSON_TYPE)
                .delete();

        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
    }
}

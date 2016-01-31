package org.koenighotze.jee7hotel.guest.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.jee7hotel.framework.integration.AbstractBaseArquillianIT;
import org.koenighotze.jee7hotel.guest.domain.Guest;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static java.util.UUID.randomUUID;
import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.HttpHeaders.LOCATION;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.fest.assertions.Assertions.assertThat;
import static org.koenighotze.jee7hotel.framework.integration.BaseArquillianSetup.createStandardDeployment;
import static org.koenighotze.jee7hotel.framework.integration.RestAssertions.*;

/**
 * @author dschmitz
 */
@RunWith(Arquillian.class)
public class GuestResourceIT extends AbstractBaseArquillianIT {
    @Deployment
    public static Archive<?> createMicroDeployment() {
        return createStandardDeployment(GuestServiceArquillianIT.class.getPackage());
    }

    @Override
    protected String getBaseServiceUrl() {
        return "rest/guests/";
    }

    @Test
    @RunAsClient
    public void a_guest_can_be_created_using_post() {
        WebTarget webTarget = client.target(getTargetUrl());

        final Guest guest = new Guest("", "Bratislav", "metulski@foobar.com");
        Response response = webTarget
                .request(APPLICATION_JSON_TYPE)
                .post(json(guest));

        assertCreated(response);
        final String location = response.getHeaderString(LOCATION);
        assertThat(location).isNotEmpty();

        Response getResponse = client.target(location).request(APPLICATION_JSON_TYPE).get();
        assertOk(getResponse);

        final Guest createdGuest = getResponse.readEntity(Guest.class);
        assertThat(createdGuest).isNotNull();
        assertThat(location).endsWith(createdGuest.getPublicId());
        assertThat(guest.getEmail()).isEqualTo("metulski@foobar.com");
        assertThat(guest.getName()).isEqualTo("Bratislav");
    }



    @Test
    @RunAsClient
    public void unknown_guests_result_in_a_not_found() {
        Response response = client.target(getTargetUrl()).path("notthere").request(APPLICATION_JSON_TYPE).get();
        assertNotFound(response);
    }

    @Test
    @RunAsClient
    public void guests_can_be_fetched_via_the_rest_api() throws InterruptedException {
        WebTarget webTarget = client.target(getTargetUrl());

        Guest[] guests = webTarget
                .request(APPLICATION_JSON_TYPE)
                .get(Guest[].class);

        assertThat(guests.length).isGreaterThan(0);
    }

    @Test
    @RunAsClient
    public void deleting_existing_guest() throws InterruptedException {
        WebTarget webTarget = client.target(getTargetUrl());

        Response response = webTarget
                .request(APPLICATION_JSON_TYPE)
                .post(json(new Guest("", "Bratislav", "metulski@foobar.com")));

        assertCreated(response);
        final String location = response.getHeaderString(LOCATION);

        final Guest guest = client.target(location).request(APPLICATION_JSON_TYPE).get(Guest.class);

        response = webTarget
                .path(guest.getPublicId())
                .request(APPLICATION_JSON_TYPE)
                .delete();

        assertOk(response);
    }

    @Test
    @RunAsClient
    public void deleting_existing_unknown_guest_results_in_not_found() throws InterruptedException {
        WebTarget webTarget = client.target(getTargetUrl());

        Response response = webTarget
                .path(randomUUID().toString())
                .request(APPLICATION_JSON_TYPE)
                .delete();

        assertNotFound(response);
    }
}

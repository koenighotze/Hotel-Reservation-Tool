package org.koenighotze.jee7hotel.booking.business.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.jee7hotel.booking.business.BookingService;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URL;

import static org.fest.assertions.Assertions.assertThat;
import static org.koenighotze.jee7hotel.framework.integration.BaseArquillianSetup.createStandardDeployment;

/**
 * @author dschmitz
 */
@RunWith(Arquillian.class)
public class BookingServiceRestIT {
    private Client client;

    @Deployment
    public static WebArchive createMicroDeployment() {
        return createStandardDeployment(BookingService.class.getPackage());
    }

    @Before
    public void setup() {
        client = ClientBuilder.newClient();
    }

    @After
    public void teardown() {
        if (null != client) {
            client.close();
        }
    }

    @Test
    @RunAsClient
    public void smoke_test(@ArquillianResource URL baseURI) throws InterruptedException {
        String targetUrl = baseURI + "rest/bookings";

        WebTarget webTarget = client.target(targetUrl);

        Response response = webTarget
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

    @Test
    @RunAsClient
    public void fetching_a_non_existing_guest_results_in_not_found(@ArquillianResource URL baseURI) throws InterruptedException {
        String targetUrl = baseURI + "rest/bookings/1234";

        WebTarget webTarget = client.target(targetUrl);

        Response response = webTarget
                .request(MediaType.APPLICATION_JSON)
                .get();

        System.err.println(response.toString());

        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

}

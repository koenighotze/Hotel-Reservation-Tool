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
import org.koenighotze.jee7hotel.booking.business.ReservationCostCalculator;
import org.koenighotze.jee7hotel.booking.business.json.ReservationBodyReader;
import org.koenighotze.jee7hotel.booking.business.json.ReservationBodyWriter;
import org.koenighotze.jee7hotel.booking.domain.Reservation;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.net.URL;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.HttpHeaders.LOCATION;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.koenighotze.jee7hotel.framework.integration.BaseArquillianSetup.createStandardDeployment;

/**
 * @author dschmitz
 */
@RunWith(Arquillian.class)
public class BookingResourceIT {
    private Client client;

    @Deployment
    public static WebArchive createMicroDeployment() {
        return createStandardDeployment(BookingResourceIT.class.getPackage());
    }

    @Before
    public void setup() {
        client = ClientBuilder.newClient();
        client.register(new ReservationBodyReader(new ReservationCostCalculator())).register(ReservationBodyWriter.class);
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
                .request(APPLICATION_JSON)
                .get();

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
    }

    @Test
    @RunAsClient
    public void fetching_a_non_existing_reservation_results_in_not_found(@ArquillianResource URL baseURI) throws InterruptedException {
        String targetUrl = baseURI + "rest/bookings/1234";

        WebTarget webTarget = client.target(targetUrl);

        Response response = webTarget
                .request(APPLICATION_JSON)
                .get();

        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
    }

    @RunAsClient
    @Test
    public void adding_a_booking_via_rest_returns_the_new_location(@ArquillianResource URL baseURI) {
        String targetUrl = baseURI + "rest/bookings/";

        WebTarget webTarget = client.target(targetUrl);

        Response response = webTarget
                .request(APPLICATION_JSON)
                .post(entity("{\"guestId\":\"9999\",\"reservationNumber\":\"abc-123\",\"checkinDate\":\"2013-01-01\",\"checkoutDate\":\"2013-02-01\",\"assignedRoomId\":\"999\",\"reservationStatus\":\"OPEN\",\"costsInEuro\":242.23}", APPLICATION_JSON));

        assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());
        assertThat(response.getHeaderString(LOCATION)).isNotEmpty();
    }

    @RunAsClient
    @Test
    public void reservations_can_be_found_by_their_public_id(@ArquillianResource URL baseURI) {
        String targetUrl = baseURI + "rest/bookings/";

        WebTarget webTarget = client.target(targetUrl);

        Response response = webTarget
                .request(APPLICATION_JSON)
                .post(entity("{\"guestId\":\"9999\",\"reservationNumber\":\"abc-123\",\"checkinDate\":\"2013-01-01\",\"checkoutDate\":\"2013-02-01\",\"assignedRoomId\":\"999\",\"reservationStatus\":\"OPEN\"}", APPLICATION_JSON));

        assertEquals(CREATED.getStatusCode(), response.getStatus());

        final String expectedPublicId = response.getHeaderString(LOCATION);
        assertNotNull(expectedPublicId);

        webTarget = client.target(response.getHeaderString(LOCATION));
        Reservation reservation = webTarget.request(APPLICATION_JSON)
                .get(Reservation.class);

        assertThat(expectedPublicId).endsWith(reservation.getReservationNumber());
        assertThat(reservation.getCostsInEuro()).isNotNull();
        assertThat(reservation.getReservationStatus()).isNotNull();
    }
}

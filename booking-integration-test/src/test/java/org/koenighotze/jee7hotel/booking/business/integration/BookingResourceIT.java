package org.koenighotze.jee7hotel.booking.business.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.jee7hotel.booking.business.ReservationCostCalculator;
import org.koenighotze.jee7hotel.booking.business.json.ReservationBodyReader;
import org.koenighotze.jee7hotel.booking.business.json.ReservationBodyWriter;
import org.koenighotze.jee7hotel.booking.domain.Reservation;
import org.koenighotze.jee7hotel.framework.integration.AbstractBaseArquillianIT;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.HttpHeaders.LOCATION;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.fest.assertions.Assertions.assertThat;
import static org.koenighotze.jee7hotel.framework.integration.BaseArquillianSetup.createStandardDeployment;
import static org.koenighotze.jee7hotel.framework.integration.RestAssertions.*;

/**
 * @author dschmitz
 */
@RunWith(Arquillian.class)
public class BookingResourceIT extends AbstractBaseArquillianIT {
    @Deployment
    public static WebArchive createMicroDeployment() {
        return createStandardDeployment(BookingResourceIT.class.getPackage());
    }

    @Before
    public void setup() {
        client.register(new ReservationBodyReader(new ReservationCostCalculator())).register(ReservationBodyWriter.class);
    }

    @Test
    @RunAsClient
    public void smoke_test() throws InterruptedException {
        WebTarget webTarget = client.target(getTargetUrl());

        Response response = webTarget
                .request(APPLICATION_JSON)
                .get();

        assertOk(response);
    }

    @Test
    @RunAsClient
    public void fetching_a_non_existing_reservation_results_in_not_found() throws InterruptedException {
        WebTarget webTarget = client.target(getTargetUrl()).path("1234");

        Response response = webTarget
                .request(APPLICATION_JSON)
                .get();

        assertNotFound(response);
    }

    @RunAsClient
    @Test
    public void adding_a_booking_via_rest_returns_the_new_location() {
        WebTarget webTarget = client.target(getTargetUrl());

        Response response = webTarget
                .request(APPLICATION_JSON)
                .post(entity("{\"guestId\":\"9999\",\"reservationNumber\":\"abc-123\",\"checkinDate\":\"2013-01-01\",\"checkoutDate\":\"2013-02-01\",\"assignedRoomId\":\"999\",\"reservationStatus\":\"OPEN\",\"costsInEuro\":242.23}", APPLICATION_JSON));

        assertCreated(response);
    }

    @RunAsClient
    @Test
    public void reservations_can_be_found_by_their_public_id() {
        WebTarget webTarget = client.target(getTargetUrl());

        Response response = webTarget
                .request(APPLICATION_JSON)
                .post(entity("{\"guestId\":\"9999\",\"reservationNumber\":\"abc-123\",\"checkinDate\":\"2013-01-01\",\"checkoutDate\":\"2013-02-01\",\"assignedRoomId\":\"999\",\"reservationStatus\":\"OPEN\"}", APPLICATION_JSON));

        assertCreated(response);

        String expectedPublicId = response.getHeaderString(LOCATION);

        webTarget = client.target(response.getHeaderString(LOCATION));
        Reservation reservation = webTarget.request(APPLICATION_JSON)
                .get(Reservation.class);

        assertThat(expectedPublicId).endsWith(reservation.getReservationNumber());
        assertThat(reservation.getCostsInEuro()).isNotNull();
        assertThat(reservation.getReservationStatus()).isNotNull();
    }

    @Override
    protected String getBaseServiceUrl() {
        return "rest/bookings/";
    }
}

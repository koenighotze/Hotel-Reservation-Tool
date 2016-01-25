package org.koenighotze.jee7hotel.booking.business.json;

import org.junit.Test;
import org.koenighotze.jee7hotel.booking.business.ReservationCostCalculator;
import org.koenighotze.jee7hotel.booking.domain.Reservation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;

import static java.time.LocalDate.of;
import static org.fest.assertions.Assertions.assertThat;
import static org.koenighotze.jee7hotel.booking.domain.ReservationStatus.OPEN;

/**
 * @author dschmitz
 */
public class ReservationBodyReaderTest {
    @Test
    public void a_json_reservation_can_be_read() throws IOException {
        String json = "{\"guest\":\"9999\",\"reservationNumber\":\"abc-123\",\"checkinDate\":\"2013-01-01\",\"checkoutDate\":\"2013-02-01\",\"assignedRoom\":\"999\",\"reservationStatus\":\"CLOSED\",\"costsInEuro\":242.23}";

        Reservation reservation = new ReservationBodyReader(new ReservationCostCalculator()).readFrom(Reservation.class, null, null, null, null, new ByteArrayInputStream(json.getBytes()));
        assertThat(reservation).isNotNull();
        assertThat(reservation.getGuest()).isEqualTo("9999");
        assertThat(reservation.getReservationNumber()).isEqualTo("abc-123");
        assertThat(reservation.getCostsInEuro()).isEqualTo(new BigDecimal("242.23"));
        assertThat(reservation.getReservationStatus()).isEqualTo(OPEN);
        assertThat(reservation.getCheckinDate()).isEqualTo(of(2013, 1, 1));
        assertThat(reservation.getCheckoutDate()).isEqualTo(of(2013, 2, 1));
        assertThat(reservation.getAssignedRoom()).isEqualTo("999");
    }
}
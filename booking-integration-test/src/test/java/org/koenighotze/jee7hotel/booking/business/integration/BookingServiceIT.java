package org.koenighotze.jee7hotel.booking.business.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.jee7hotel.booking.business.BookingService;
import org.koenighotze.jee7hotel.booking.domain.Reservation;
import org.koenighotze.jee7hotel.framework.integration.BaseArquillianSetup;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.koenighotze.jee7hotel.booking.domain.ReservationStatus.CANCELED;

/**
 * @author dschmitz
 */
@RunWith(Arquillian.class)
public class BookingServiceIT {
    private static final Logger LOGGER = Logger.getLogger(BookingServiceIT.class.getName());

    @Inject
    private BookingService bookingService;

    @Deployment
    public static Archive<?> createMicroDeployment() {
        return BaseArquillianSetup.createStandardDeployment(BookingServiceIT.class.getPackage());
    }

    @Test
    public void open_reservations_can_be_cancelled() {
        final Reservation reservation = bookingService.bookRoom("abc", "123", LocalDate.now(), LocalDate.now().plusDays(1));

        bookingService.cancelReservation(reservation.getReservationNumber());

        assertThat(bookingService.findReservationByNumber(reservation.getReservationNumber()).get().getReservationStatus(), is(equalTo(CANCELED)));
    }
}

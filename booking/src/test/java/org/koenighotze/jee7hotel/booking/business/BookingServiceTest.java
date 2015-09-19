

package org.koenighotze.jee7hotel.booking.business;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.jee7hotel.booking.business.events.NewReservationEvent;
import org.koenighotze.jee7hotel.booking.business.events.ReservationStatusChangeEvent;
import org.koenighotze.jee7hotel.booking.domain.Reservation;
import org.koenighotze.jee7hotel.booking.domain.RoomEquipment;
import org.koenighotze.jee7hotel.business.AbstractBasePersistenceTest;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.enterprise.event.Event;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static java.time.LocalDate.now;
import static org.fest.assertions.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class BookingServiceTest extends AbstractBasePersistenceTest {

    private BookingService bookingService;

    @Mock
    private Event<NewReservationEvent> mockEvent;

    @Mock
    private Event<ReservationStatusChangeEvent> mockResEvent;

    @Override
    protected void initHook() {
        bookingService = new BookingService(mockEvent, mockResEvent);

        bookingService.setEntityManager(super.getEntityManager());
    }

    @Override
    protected String getPersistenceUnitName() {
        return "booking-integration-test";
    }

    @Test
    public void cancel_returns_true_if_reservation_was_cancelled() {
        Reservation reservation =
                bookingService.bookRoom("guest", "room", now(), now());

        assertThat(bookingService.cancelReservation(reservation.getReservationNumber()));
    }

    @Test
    public void cancel_returns_false_if_reservation_was_not_found() {

    }

    @Test
    public void testGetAllReservations() {
        List<Reservation> allReservations = bookingService.getAllReservations();
        assertThat(allReservations).isNotNull();
    }

    @Test
    public void testBookRoom() {
        Reservation reservation =
                bookingService.bookRoom("guest", "room", now(), now());
        getEntityManager().flush();
        assertThat(reservation).isNotNull();
        assertThat(reservation.getReservationNumber()).isNotNull();
    }

    @Test
    public void calcRate() {
        LocalDate from = LocalDate.of(1976, Month.SEPTEMBER, 8);
        LocalDate to = LocalDate.of(1976, Month.SEPTEMBER, 12);
        BigDecimal rate = bookingService.calculateRateFor(RoomEquipment.BUDGET, from, to);
        assertThat(rate).isEqualTo(BigDecimal.valueOf(240L));
    }

    @Test
    public void findReservationsForGuest() {
        List<Reservation> reservations = bookingService.findReservationForGuest("aguest");

        assertThat(reservations).isEmpty();

        bookingService.bookRoom("aguest", "aroom", now(), now());

        reservations = bookingService.findReservationForGuest("aguest");
        assertThat(reservations.size()).isEqualTo(1);
    }
}

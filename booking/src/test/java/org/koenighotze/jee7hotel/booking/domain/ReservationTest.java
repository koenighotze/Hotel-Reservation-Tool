package org.koenighotze.jee7hotel.booking.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.jee7hotel.booking.business.BookingService;
import org.koenighotze.jee7hotel.booking.business.events.NewReservationEvent;
import org.koenighotze.jee7hotel.booking.business.events.ReservationStatusChangeEvent;
import org.koenighotze.jee7hotel.business.AbstractBasePersistenceTest;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.enterprise.event.Event;

import static java.math.BigDecimal.ONE;
import static java.time.LocalDate.now;
import static org.fest.assertions.Assertions.assertThat;
import static org.koenighotze.jee7hotel.booking.domain.ReservationStatus.OPEN;

@RunWith(MockitoJUnitRunner.class)
public class ReservationTest extends AbstractBasePersistenceTest {
    private static final String WELL_KNOWN_ID = "9999";
    private static final String WELL_KNOWN_ROOM_NUMBER = "001";

    private BookingService bookingService;
    @Mock
    private Event<NewReservationEvent> mockEvent;

    @Mock
    private Event<ReservationStatusChangeEvent> mockResEvent;

    @Override
    protected String getPersistenceUnitName() {
        return "booking-integration-test";
    }

    @Override
    protected void initHook() {
        super.initHook();
        this.bookingService = new BookingService(this.mockEvent, this.mockResEvent);

        this.bookingService.setEntityManager(getEntityManager());
    }

    @Test
    public void a_reservation_can_be_saved() {
        Reservation reservation = this.bookingService.bookRoom(WELL_KNOWN_ID, WELL_KNOWN_ROOM_NUMBER, now(), now());
        assertThat(reservation).isNotNull();
        getEntityManager().flush();
    }

    @Test
    public void an_open_reservation_is_reported_as_being_open() {
        Reservation reservation = new Reservation("guest", "res", "room", now(), now(), ONE);
        assertThat(reservation.getReservationStatus()).isEqualTo(OPEN);
        assertThat(reservation.isOpen()).isTrue();
    }
}
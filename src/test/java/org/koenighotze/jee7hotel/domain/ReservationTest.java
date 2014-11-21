package org.koenighotze.jee7hotel.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.jee7hotel.business.AbstractBasePersistenceTest;
import org.koenighotze.jee7hotel.business.BookingService;
import org.koenighotze.jee7hotel.business.GuestService;
import org.koenighotze.jee7hotel.business.RoomService;
import org.koenighotze.jee7hotel.business.events.NewReservationEvent;
import org.koenighotze.jee7hotel.business.events.ReservationStatusChangeEvent;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.enterprise.event.Event;
import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ReservationTest extends AbstractBasePersistenceTest {
    private static final Long WELL_KNOWN_ID = 9999L;
    private static final String WELL_KNOWN_ROOM_NUMBER = "001";

    private RoomService roomService = new RoomService();
    private BookingService bookingService = new BookingService();
    private GuestService guestService = new GuestService();
    @Mock
    private Event<NewReservationEvent> mockEvent;

    @Mock
    private Event<ReservationStatusChangeEvent> mockResEvent;

    @Override
    protected void initHook() {
        super.initHook();
        this.bookingService.setEntityManager(super.getEntityManager());
        this.bookingService.setReservationEvents(this.mockEvent);
        this.bookingService.setReservationStateChangeEvents(this.mockResEvent);

        this.guestService.setEntityManager(super.getEntityManager());
        this.roomService.setEntityManager(super.getEntityManager());


    }

    @Test
    public void testSave() {
        Optional<Guest> guest = this.guestService.findById(WELL_KNOWN_ID);
        Optional<Room> room = this.roomService.findRoomByNumber(WELL_KNOWN_ROOM_NUMBER);
        Reservation reservation = this.bookingService.bookRoom(guest.get(), room.get(), LocalDate.now(), LocalDate.now());
        assertThat(reservation, is(not(nullValue())));
        getEntityManager().flush();
    }


}
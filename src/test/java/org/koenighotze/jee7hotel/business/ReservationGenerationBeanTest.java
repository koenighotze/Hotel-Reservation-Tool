package org.koenighotze.jee7hotel.business;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.jee7hotel.business.events.BookingMessageTO;
import org.koenighotze.jee7hotel.domain.Guest;
import org.koenighotze.jee7hotel.domain.Room;
import org.koenighotze.jee7hotel.domain.RoomEquipment;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jms.JMSException;
import javax.jms.Message;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReservationGenerationBeanTest {
    private ReservationGenerationBean reservationGenerationBean;

    @Mock
    private RoomService roomService;
    @Mock
    private GuestService guestService;
    @Mock
    private BookingService bookingService;

    @Test
    public void testIncomingReservationMessage() throws JMSException {
        final Long guestId = 12L;
        String roomNumber = "abc";

        final String payload = new BookingMessageTO(guestId, roomNumber, LocalDate.now(), LocalDate.now()).toJson();
        Optional<Guest> guest = Optional.of(new Guest("", ""));
        when(this.guestService.findById(guestId)).thenReturn(guest);
        Optional<Room> room = Optional.of(new Room(roomNumber, RoomEquipment.BUDGET));
        when(this.roomService.findRoomByNumber(roomNumber)).thenReturn(room);

        this.reservationGenerationBean = new ReservationGenerationBean(this.roomService, this.guestService, this.bookingService);

        Message message = mock(Message.class);
        when(message.getBody(String.class)).thenReturn(payload);

        this.reservationGenerationBean.onMessage(message);

        verify(this.bookingService).bookRoom(guest.get(), room.get(), LocalDate.now(), LocalDate.now());
    }



}
package org.koenighotze.jee7hotel.booking.business;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jms.JMSException;

@RunWith(MockitoJUnitRunner.class)
public class ReservationGenerationBeanTest {
    private ReservationGenerationBean reservationGenerationBean;

//    @Mock
//    private RoomService roomService;
//    @Mock
//    private GuestService guestService;
    @Mock
    private BookingService bookingService;

    @Test
    public void testIncomingReservationMessage() throws JMSException {
//        final Long guestId = 12L;
//        String roomNumber = "abc";
//
//        final String payload = new BookingMessageTO(guestId, roomNumber, LocalDate.now(), LocalDate.now()).toJson();
////        Optional<Guest> guest = Optional.of(new Guest("", ""));
////        when(this.guestService.findById(guestId)).thenReturn(guest);
////        Optional<Room> room = Optional.of(new Room(roomNumber, RoomEquipment.BUDGET));
////        when(this.roomService.findRoomByNumber(roomNumber)).thenReturn(room);
//
//        this.reservationGenerationBean = new ReservationGenerationBean(this.bookingService);
//
//        Message message = mock(Message.class);
//        when(message.getBody(String.class)).thenReturn(payload);
//
//        this.reservationGenerationBean.onMessage(message);
//
//        verify(this.bookingService).bookRoom("12", "abc", LocalDate.now(), LocalDate.now());
    }



}
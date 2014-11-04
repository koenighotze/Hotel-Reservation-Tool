

package demo.business;

import demo.domain.Guest;
import demo.domain.Reservation;
import demo.domain.Room;
import java.util.Date;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author dschmitz
 */
public class BookingServiceTest extends AbstractBasePersistenceTest {
    private static final Long WELL_KNOWN_ID = 9999L;
     private static final String WELL_KNOWN_ROOM_NUMBER = "001";
    
    private RoomService roomService = new RoomService();
    private BookingService bookingService = new BookingService();
    private GuestService guestService = new GuestService();
    
    
//    public void cancelReservation(Reservation reservation) { }
//    
//     public List<Reservation> findReservationForGuest(Guest guest) {  
//         
//         
//     }
//     
//     public List<Reservation> getAllReservations()
//             
//              Reservation bookRoom(Guest guest, Room room, Date checkin, Date checkout) {

    @Override
    protected void initHook() {
        this.bookingService.setEntityManager(super.getEntityManager());
        this.guestService.setEntityManager(super.getEntityManager());
        this.roomService.setEntityManager(super.getEntityManager());
    }
    
    @Test
    public void testGetAllReservations() {
        List<Reservation> allReservations = this.bookingService.getAllReservations();
        assertThat(allReservations, is(not(nullValue())));
    }
    
    @Test
    public void testBookRoom() {
        Guest guest = this.guestService.findById(WELL_KNOWN_ID);
        Room room = this.roomService.findRoomByNumber(WELL_KNOWN_ROOM_NUMBER);
        Date from = new Date();
        Date to = new Date();
        Reservation reservation = this.bookingService.bookRoom(guest, room, from, to);
        getEntityManager().flush();
        assertThat(reservation, is(not(nullValue())));
        assertThat(reservation.getReservationNumber(), is(not(nullValue())));
    }
}

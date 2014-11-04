

package demo.business;

import demo.domain.Room;
import java.util.List;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author dschmitz
 */
public class RoomServiceTest extends AbstractBasePersistenceTest {
    private static final Long WELL_KNOWN_ROOM_ID = 999L;
    private static final String WELL_KNOWN_ROOM_NUMBER = "001";
    
    private RoomService roomService;

    
    
    @Override
    protected void initHook() {
        this.roomService = new RoomService();
        this.roomService.setEntityManager(getEntityManager());
    }

    @Override
    protected void cleanupHook() {
        this.roomService = null;
    }

    @Test
    public void testGetAllRooms() {
        List<Room> rooms = this.roomService.getAllRooms();
        
        assertThat(rooms, is(not(nullValue())));
        assertFalse(rooms.isEmpty()); 
    }
    
    @Test
    public void testFindByIdKnownRoom() {
        Room room = this.roomService.findRoomById(WELL_KNOWN_ROOM_ID);
        assertThat(room, is(not(nullValue())));
        assertThat(room.getId(), is(equalTo(WELL_KNOWN_ROOM_ID)));
    }
    
    @Test
    public void testFindByIdUnkownRoom() {
        Room room = this.roomService.findRoomById(1234L);
        assertThat(room, is(nullValue()));
    }    
    
    @Test
    public void testFindByNumber() {
        Room room = this.roomService.findRoomByNumber(WELL_KNOWN_ROOM_NUMBER);
        assertThat(room, is(not(nullValue())));
        assertThat(room.getId(), is(equalTo(WELL_KNOWN_ROOM_ID)));
    }
}

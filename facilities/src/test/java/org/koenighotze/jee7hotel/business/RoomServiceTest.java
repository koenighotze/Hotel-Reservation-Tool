

package org.koenighotze.jee7hotel.business;

import org.junit.Test;
import org.koenighotze.jee7hotel.domain.Room;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
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
        Room room = this.roomService.findRoomById(WELL_KNOWN_ROOM_ID).get();
        assertThat(room, is(not(nullValue())));
        assertThat(room.getId(), is(equalTo(WELL_KNOWN_ROOM_ID)));
    }

    @Test
    public void testFindByIdUnkownRoom() {
        Optional<Room> room = this.roomService.findRoomById(1234L);
        assertFalse(room.isPresent());
    }

    @Test
    public void testFindByNumber() {
        Room room = this.roomService.findRoomByNumber(WELL_KNOWN_ROOM_NUMBER).get();
        assertThat(room, is(not(nullValue())));
        assertThat(room.getId(), is(equalTo(WELL_KNOWN_ROOM_ID)));
    }
}

package org.koenighotze.jee7hotel.domain;

import org.junit.Test;
import org.koenighotze.jee7hotel.business.AbstractBasePersistenceTest;

import javax.persistence.Cache;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;


/**
 * @author koenighotze
 */
public class RoomCacheTest extends AbstractBasePersistenceTest {
    private static final Long WELL_KNOWN_ROOM_ID = 999L;

    @Test
    public void testRoomsAreCached() {
        Room room = getEntityManager().find(Room.class, WELL_KNOWN_ROOM_ID);

        assertThat(room, is(not(nullValue())));

        Cache cache = getEntityManager().getEntityManagerFactory().getCache();
        assertTrue("Rooms should be cached ", cache.contains(Room.class, room.getId()));
    }


    @Test
    public void testTransientRoomsAreNotCached() {
        Room room = new Room("", RoomEquipment.BUDGET);

        Cache cache = getEntityManager().getEntityManagerFactory().getCache();
        assertFalse("Rooms should be cached ", cache.contains(Room.class, room.getId()));
    }
}

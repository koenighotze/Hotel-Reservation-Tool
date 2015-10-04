package org.koenighotze.jee7hotel.guest.business.guestdataimport;

import org.junit.Test;
import org.koenighotze.jee7hotel.framework.test.AbstractBasePersistenceTest;
import org.koenighotze.jee7hotel.guest.business.GuestService;
import org.koenighotze.jee7hotel.guest.domain.Guest;

import javax.enterprise.event.Event;
import javax.persistence.TypedQuery;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class GuestDataWriterTest extends AbstractBasePersistenceTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testWriteItem() throws Exception {
        GuestService guestService = new GuestService(mock(Event.class));
        guestService.setEntityManager(getEntityManager());

        assertThat(getEntityManager(), is(not(nullValue())));

        GuestDataWriter guestDataWriter = new GuestDataWriter();
        guestDataWriter.setEntityManager(getEntityManager());
        guestDataWriter.setGuestService(guestService);

        List<Object> items = asList(new Guest("dsa", "foo", "baz@bar.com"));
        guestDataWriter.writeItems(items);

        // check if the items where written to the PC

        TypedQuery<Guest> query = getEntityManager().createNamedQuery("Guest.findByName", Guest.class);

        assertThat(query.setParameter("name", "foo").getSingleResult(), is(not(nullValue())));
    }
}

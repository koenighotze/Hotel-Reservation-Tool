package org.koenighotze.jee7hotel.business.guestdataimport;

import org.junit.Test;
import org.koenighotze.jee7hotel.business.AbstractBasePersistenceTest;
import org.koenighotze.jee7hotel.business.GuestService;
import org.koenighotze.jee7hotel.domain.Guest;

import javax.jms.JMSContext;
import javax.persistence.TypedQuery;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class GuestDataWriterTest extends AbstractBasePersistenceTest {

    @Test
    public void testWriteItem() throws Exception {
        GuestService guestService = new GuestService(mock(JMSContext.class));
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

package org.koenighotze.jee7hotel.batch.guestdataimport;

import org.junit.Ignore;
import org.junit.Test;
import org.koenighotze.jee7hotel.business.AbstractBasePersistenceTest;
import org.koenighotze.jee7hotel.business.GuestService;
import org.koenighotze.jee7hotel.domain.Guest;

import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class GuestDataWriterTest extends AbstractBasePersistenceTest {

    @Test
    public void testWriteItem() throws Exception {
        GuestService guestService = new GuestService();
        guestService.setEntityManager(getEntityManager());

        assertThat(getEntityManager(), is(not(nullValue())));

        GuestDataWriter guestDataWriter = new GuestDataWriter();
        guestDataWriter.setEntityManager(getEntityManager());
        guestDataWriter.setGuestService(guestService);

        List<Object> items = Arrays.asList(new Guest("foo", "baz@bar.com"));
        guestDataWriter.writeItems(items);

        // check if the items where written to the PC

        TypedQuery<Guest> query = getEntityManager().createNamedQuery("Guest.findByName", Guest.class);

        assertThat(query.setParameter("name", "foo").getSingleResult(), is(not(nullValue())));

    }

}

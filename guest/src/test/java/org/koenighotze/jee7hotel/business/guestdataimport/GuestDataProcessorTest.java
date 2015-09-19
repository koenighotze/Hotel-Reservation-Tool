package org.koenighotze.jee7hotel.business.guestdataimport;

import org.junit.Test;
import org.koenighotze.jee7hotel.domain.Guest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GuestDataProcessorTest {
    @Test
    public void processReturnsGuest() throws Exception {
        Object processItem = new GuestDataProcessor().processItem("id, foo bar, bratzen@putz.de");
        Guest guest = (Guest) processItem;

        assertThat("foo bar", is(equalTo(guest.getName())));
        assertThat("bratzen@putz.de", is(equalTo(guest.getEmail())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void processThrowsExceptionIfInvalidEmail() throws Exception {
        new GuestDataProcessor().processItem("foo bar, invalidemail.de");
    }

}

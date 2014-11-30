package org.koenighotze.jee7hotel.batch.guestdataimport;

import org.junit.Test;
import org.koenighotze.jee7hotel.domain.Guest;

import java.util.IllegalFormatException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class GuestDataProcessorTest {
    @Test
    @org.junit.Ignore
    public void processReturnsGuest() throws Exception {
        Object processItem = new GuestDataProcessor().processItem("foo bar, bratzen@putz.de");
        Guest guest = (Guest) processItem;

        assertThat("foo bar", is(equalTo(guest.getName())));
        assertThat("bratzen@putz.de", is(equalTo(guest.getEmail())));
    }

    @org.junit.Ignore
    @Test(expected = IllegalFormatException.class)
    public void processThrowsExceptionIfInvalidEmail() throws Exception {
        new GuestDataProcessor().processItem("foo bar, invalidemail.de");
    }

}

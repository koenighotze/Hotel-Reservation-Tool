package org.koenighotze.jee7hotel.booking.business.events;

import org.junit.Test;
import org.koenighotze.jee7hotel.booking.business.events.BookingMessageTO;

import java.time.LocalDate;
import java.time.Month;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BookingMessageTOTest {
    private static final String JSON = "{\"guestId\":12,\"roomNumber\":\"abc\",\"checkin\":\"2014-11-25\",\"checkout\":\"2014-11-25\"}";


    @Test
    public void fromJson() {
        BookingMessageTO fromJson = BookingMessageTO.fromJson(JSON);

        assertThat(fromJson.getGuest(), is(equalTo(12L)));
        assertThat(fromJson.getRoom(), is(equalTo("abc")));
        assertThat(fromJson.getCheckin(), is(equalTo(LocalDate.of(2014, Month.NOVEMBER, 25))));
        assertThat(fromJson.getCheckout(), is(equalTo(LocalDate.of(2014, Month.NOVEMBER, 25))));
    }

    @Test
    public void toJson() {
        LocalDate aDate = LocalDate.of(2014, Month.NOVEMBER, 25);

        String generatedjson = new BookingMessageTO(12L, "abc", aDate, aDate).toJson();
        assertThat(generatedjson, is(equalTo(JSON)));
    }

}
package org.koenighotze.jee7hotel.frontend.converter;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class LocalDateConverterTest {
    @Test
    public void testConvertToRFC3339Date() {
        final String expected = "2014-11-11";

        String result = new LocalDateConverter().getAsString(null, null, LocalDate.of(2014, Month.NOVEMBER, 11));
        assertThat(result, is(equalTo(expected)));

    }

    @Test
    public void testChromeDate() {
        String input = "2014-11-11";

        LocalDate result = (LocalDate) new LocalDateConverter().getAsObject(null, null, input);

        assertThat(result, is(not(nullValue())));
        assertThat(result.getDayOfMonth(), is(equalTo(11)));
        assertThat(result.getMonth(), is(equalTo(Month.NOVEMBER)));
        assertThat(result.getYear(), is(equalTo(2014)));

    }
}
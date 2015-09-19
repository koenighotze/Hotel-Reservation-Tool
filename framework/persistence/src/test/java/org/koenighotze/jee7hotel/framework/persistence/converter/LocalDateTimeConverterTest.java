package org.koenighotze.jee7hotel.framework.persistence.converter;

import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static java.time.Month.APRIL;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * @author dschmitz
 */
public class LocalDateTimeConverterTest {

    @Test
    public void null_values_are_converted_to_null() {
        assertNull(new LocalDateTimeConverter().convertToDatabaseColumn(null));
    }

    @Test
    public void localdatetimes_are_converted_to_timestamps() {
        final LocalDateTime localDate = LocalDateTime.of(1999, APRIL, 12, 10, 12, 10, 10);

        Timestamp timestamp = new LocalDateTimeConverter().convertToDatabaseColumn(localDate);

        assertThat(timestamp, is(equalTo(Timestamp.valueOf(localDate))));
    }

    @Test
    public void null_values_are_read_as_nulls() {
        assertNull(new LocalDateTimeConverter().convertToEntityAttribute(null));
    }

}
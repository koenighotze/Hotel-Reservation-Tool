package org.koenighotze.jee7hotel.framework.persistence.converter;

import org.junit.Test;

import java.sql.Date;
import java.time.LocalDate;

import static java.time.Month.APRIL;
import static java.time.Month.FEBRUARY;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * @author dschmitz
 */
public class LocalDateConverterTest {
    @Test
    public void null_values_are_converted_to_null() {
        assertNull(new LocalDateConverter().convertToDatabaseColumn(null));
    }

    @Test
    public void localdates_are_converted_to_simple_dates_keeping_only_the_datefields() {
        final LocalDate localDate = LocalDate.of(1999, APRIL, 12);

        Date date = new LocalDateConverter().convertToDatabaseColumn(localDate);

        assertThat(date, is(equalTo(Date.valueOf(localDate))));
    }

    @Test
    public void null_values_are_read_as_nulls() {
        assertNull(new LocalDateConverter().convertToEntityAttribute(null));
    }

    @Test
    public void simple_dates_are_read_as_proper_localdates_without_timedata() {
        Date date = Date.valueOf("2012-02-12");
        LocalDate localDate = new LocalDateConverter().convertToEntityAttribute(date);

        assertThat(localDate, is(equalTo(LocalDate.of(2012, FEBRUARY, 12))));
    }
}
package org.koenighotze.jee7hotel.domain.converter;

import org.junit.Test;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class LocalDateConverterTest {

    @Test
    public void testConvertToDatabaseColumn() throws Exception {
        LocalDate localDate = LocalDate.of(1975, Month.SEPTEMBER, 8);
        assertThat(new LocalDateConverter().convertToDatabaseColumn(localDate), is(not(nullValue())));

    }

    @Test
    public void testConvertToEntityAttribute() throws Exception {
        Date date = new Date(Instant.now().getEpochSecond());
        assertThat(new LocalDateConverter().convertToEntityAttribute(date), is(not(nullValue())));
    }
    @Test
    public void testWithError() {
        Date date = new Date(Date.parse("1/1/13 12:00 AM"));
        new LocalDateConverter().convertToEntityAttribute(date);
    }
}
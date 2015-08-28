package org.koenighotze.jee7hotel.booking.persistence.converter;

import org.junit.Test;
import org.koenighotze.jee7hotel.booking.domain.ReservationStatus;

import static org.fest.assertions.Assertions.assertThat;
import static org.koenighotze.jee7hotel.booking.domain.ReservationStatus.CANCELED;

/**
 * @author dschmitz
 */
public class ReservationStatusConverterTest {
    @Test
    public void the_conversion_of_a_value_uses_the_values_name() {
        String value = new ReservationStatusConverter().convertToDatabaseColumn(CANCELED);
        assertThat(value).isEqualTo(CANCELED.name());
    }

    @Test
    public void the_conversion_of_a_null_is_the_empty_string() {
        String value = new ReservationStatusConverter().convertToDatabaseColumn(null);
        assertThat(value).isEmpty();
    }

    @Test
    public void the_entity_value_of_a_known_string_is_the_enumeration() {
        ReservationStatus value = new ReservationStatusConverter().convertToEntityAttribute(CANCELED.name());
        assertThat(value).isSameAs(CANCELED);
    }

    @Test(expected = IllegalArgumentException.class)
    public void the_entity_value_of_an_unknown_string_results_in_an_exception() {
        new ReservationStatusConverter().convertToEntityAttribute("Foo");
    }

    @Test
    public void the_entity_value_of_a_null_is_null() {
        ReservationStatus value = new ReservationStatusConverter().convertToEntityAttribute(null);
        assertThat(value).isNull();
    }

}
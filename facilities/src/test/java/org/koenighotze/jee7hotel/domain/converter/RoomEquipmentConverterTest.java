package org.koenighotze.jee7hotel.domain.converter;

import org.junit.Test;
import org.koenighotze.jee7hotel.domain.RoomEquipment;

import static org.fest.assertions.Assertions.assertThat;
import static org.koenighotze.jee7hotel.domain.RoomEquipment.BUDGET;

/**
 * @author dschmitz
 */
public class RoomEquipmentConverterTest {

    @Test
    public void a_null_equipment_is_converted_to_null() {
        String result = new RoomEquipmentConverter().convertToDatabaseColumn(null);

        assertThat(result).isNull();
    }

    @Test
    public void roomequipments_are_converted_to_their_respective_names() {
        String result = new RoomEquipmentConverter().convertToDatabaseColumn(BUDGET);
        assertThat(result).isEqualTo(BUDGET.name());
    }

    @Test
    public void null_db_values_are_converted_to_null() {
        RoomEquipment equipment = new RoomEquipmentConverter().convertToEntityAttribute(null);

        assertThat(equipment).isNull();
    }

    @Test
    public void known_values_are_converted_to_roomequipment() {
        RoomEquipment equipment = new RoomEquipmentConverter().convertToEntityAttribute(BUDGET.name());

        assertThat(equipment).isSameAs(BUDGET);
    }

    @Test(expected = IllegalArgumentException.class)
    public void converting_unknown_values_results_in_an_exception() {
        new RoomEquipmentConverter().convertToEntityAttribute("unknown");
    }
}
package org.koenighotze.jee7hotel.frontend.converter;

import org.junit.Test;
import org.koenighotze.jee7hotel.domain.RoomEquipment;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;


public class RoomEquipmentConverterTest {

    @Test
    public void testGetAsObject() throws Exception {
        Object result = new RoomEquipmentConverter().getAsObject(null, null, "BUDGET");
        assertThat(result, is(sameInstance(RoomEquipment.BUDGET)));
    }

    @Test
    public void testGetAsString() throws Exception {
        String result = new RoomEquipmentConverter().getAsString(null, null, RoomEquipment.BUDGET);

        assertThat(result, is(equalTo("BUDGET")));
    }
}
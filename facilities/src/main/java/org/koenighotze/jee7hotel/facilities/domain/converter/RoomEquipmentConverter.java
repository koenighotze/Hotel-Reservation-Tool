

package org.koenighotze.jee7hotel.facilities.domain.converter;

import org.koenighotze.jee7hotel.facilities.domain.RoomEquipment;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author dschmitz
 */
@Converter
public class RoomEquipmentConverter implements AttributeConverter<RoomEquipment, String> {

    @Override
    public String convertToDatabaseColumn(RoomEquipment x) {
        if (null == x) {
            return null;
        }
        return x.name().toUpperCase();
    }

    @Override
    public RoomEquipment convertToEntityAttribute(String y) {
        if (null == y) {
            return null;
        }

        for (RoomEquipment elem : RoomEquipment.values()) {
            if (elem.name().equalsIgnoreCase(y)) {
                return elem;
            }
        }

        throw new IllegalArgumentException("Cannot map " + y);
    }
}

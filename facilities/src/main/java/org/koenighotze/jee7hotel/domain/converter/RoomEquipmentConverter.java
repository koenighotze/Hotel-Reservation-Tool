

package org.koenighotze.jee7hotel.domain.converter;

import org.koenighotze.jee7hotel.domain.RoomEquipment;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


/**
 *
 * @author dschmitz
 */
@Converter
public class RoomEquipmentConverter implements AttributeConverter<RoomEquipment, String> {

    @Override
    public String convertToDatabaseColumn(RoomEquipment x) {
        return x.name().toUpperCase();
    }

    @Override
    public RoomEquipment convertToEntityAttribute(String y) {
        for (RoomEquipment elem : RoomEquipment.values()) {
            if (elem.name().equalsIgnoreCase(y)) {
                return elem;
            }
        }
        
        throw new IllegalArgumentException("Cannot map " + y);
    }



}

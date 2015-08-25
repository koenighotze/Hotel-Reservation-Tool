

package org.koenighotze.jee7hotel.booking.persistence.converter;

import org.koenighotze.jee7hotel.booking.domain.ReservationStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * Converter for ReservationStatus.
 *
 * @author dschmitz
 */
@Converter(autoApply = true)
public class ReservationStatusConverter implements AttributeConverter<ReservationStatus, String> {

    @Override
    public String convertToDatabaseColumn(ReservationStatus x) {
        return x.name();
    }

    @Override
    public ReservationStatus convertToEntityAttribute(String y) {
        for (ReservationStatus elem : ReservationStatus.values()) {
            if (elem.name().equalsIgnoreCase(y)) {
                return elem;
            }
        }
        
        throw new IllegalArgumentException("Cannot map " + y);
    }

}

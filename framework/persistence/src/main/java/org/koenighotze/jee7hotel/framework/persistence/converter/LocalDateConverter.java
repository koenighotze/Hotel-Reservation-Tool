package org.koenighotze.jee7hotel.framework.persistence.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;
import java.util.logging.Logger;

/**
 * Converter for integrating JDK 8 LocalDates into JPA.
 *
 * @author koenighotze
 */
@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {

    private static final Logger LOGGER = Logger.getLogger(LocalDateConverter.class.getName());

    @Override
    public Date convertToDatabaseColumn(LocalDate localDate) {
        LOGGER.finest(() -> "Converting " + localDate + " to database");
        if (null == localDate) {
            return null;
        }
        return Date.valueOf(localDate);
    }

    @Override
    public LocalDate convertToEntityAttribute(Date date) {
        LOGGER.finest(() -> "Converting " + date + " to attrib");
        if (null == date) {
            return null;
        }

        return date.toLocalDate();
    }
}

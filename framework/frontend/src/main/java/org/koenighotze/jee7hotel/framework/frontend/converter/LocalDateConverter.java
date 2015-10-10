package org.koenighotze.jee7hotel.framework.frontend.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author dschmitz
 */
@FacesConverter(forClass = LocalDate.class)
public class LocalDateConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (isBlank(value)) {
            return null;
        }

        try {
            return LocalDate.parse(value);
        }
        catch (DateTimeParseException e) {
            throw new ConverterException(new FacesMessage(SEVERITY_ERROR, "Please provide a date using a format like 2014-12-24", null), e);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (null == value) {
            return "";
        }
        return ((LocalDate) value).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}

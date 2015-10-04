package org.koenighotze.jee7hotel.framework.frontend.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

        return LocalDate.parse(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (null == value) {
            return "";
        }
        return ((LocalDate) value).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}

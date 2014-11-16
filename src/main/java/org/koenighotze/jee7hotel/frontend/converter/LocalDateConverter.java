package org.koenighotze.jee7hotel.frontend.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.logging.Logger;

/**
 * Created by dschmitz on 15.11.14.
 */
@FacesConverter(forClass = LocalDate.class)
public class LocalDateConverter implements Converter {
    private static final Logger LOGGER = Logger.getLogger(LocalDateConverter.class.getName());

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        LOGGER.fine(() -> "Get as object " + s);
        // TODO: extract pattern from component

        if (null == s) {
            return null;
        }
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).parse(s);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        LOGGER.fine(() -> "Get as string " + o);
        // TODO: extract pattern from component

        if (null == o || !(o instanceof LocalDate)) {
            return null;
        }

        return DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).format((LocalDate) o);
    }
}

package org.koenighotze.jee7hotel.framework.frontend.converter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import static java.time.Month.APRIL;
import static java.util.Locale.US;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * @author dschmitz
 */
public class LocalDateConverterTest {

    private Locale locale;

    @Before
    public void setup() {
        locale = Locale.getDefault();

        Locale.setDefault(US);
    }

    @After
    public void tearDown() {
        Locale.setDefault(locale);
    }

    @Test
    public void localdates_are_converted_to_strings_using_us_locale() {
        String value = new LocalDateConverter().getAsString(mock(FacesContext.class), mock(UIComponent.class), LocalDate.of(2012, APRIL, 3));

        assertThat(value).isEqualTo("2012-04-03");
    }

    @Test
    public void null_localdates_are_converted_to_empty_strings() {
        String value = new LocalDateConverter().getAsString(mock(FacesContext.class), mock(UIComponent.class), null);

        assertThat(value).isEqualTo("");
    }


    @Test
    public void empty_strings_are_converted_to_null_localdates() {
        Object localDate = new LocalDateConverter().getAsObject(mock(FacesContext.class), mock(UIComponent.class), "");
        assertThat(localDate).isNull();
    }

    @Test
    public void null_strings_are_converted_to_null_localdates() {
        Object localDate = new LocalDateConverter().getAsObject(mock(FacesContext.class), mock(UIComponent.class), null);
        assertThat(localDate).isNull();
    }

    @Test
    public void valid_strings_are_converted_to_matching_localdates() {
        Object localDate = new LocalDateConverter().getAsObject(mock(FacesContext.class), mock(UIComponent.class), "2012-04-05");
        assertThat(localDate).isEqualTo(LocalDate.of(2012, APRIL, 5));
    }

    @Test(expected = DateTimeParseException.class)
    public void invalid_date_strings_result_in_a_conversion_exception() {
        new LocalDateConverter().getAsObject(mock(FacesContext.class), mock(UIComponent.class), "adsdas");
    }

}
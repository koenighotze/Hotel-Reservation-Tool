package org.koenighotze.jee7hotel.booking.domain;

import org.junit.Test;

import javax.xml.bind.JAXB;
import java.io.InputStream;
import java.io.StringWriter;

import static javax.xml.bind.JAXB.unmarshal;
import static org.fest.assertions.Assertions.assertThat;

/**
 * @author dschmitz
 */
public class GuestModelTest {
    @Test
    public void a_valid_guest_string_can_be_unmarshalled() {
        InputStream stream = GuestModelTest.class.getResourceAsStream("validguest.xml");

        assertThat(stream).isNotNull();
        assertThat(unmarshal(stream, GuestModel.class)).isNotNull();
    }

    @Test
    public void marshal() {
        StringWriter w = new StringWriter();
        GuestModel guest = new GuestModel("foo", "qux");
        JAXB.marshal(guest, w);
        assertThat(w.toString()).startsWith("<?xml");
    }
}
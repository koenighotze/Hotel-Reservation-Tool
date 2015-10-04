package org.koenighotze.jee7hotel.domain;

import org.junit.Test;
import org.koenighotze.jee7hotel.business.AbstractBasePersistenceTest;
import org.koenighotze.jee7hotel.framework.persistence.audit.Audit;

import javax.xml.bind.JAXB;
import java.io.StringWriter;

import static java.time.LocalDateTime.now;
import static org.fest.assertions.Assertions.assertThat;

/**
 * @author dschmitz
 */
public class GuestTest extends AbstractBasePersistenceTest {

    @Test
    public void persisting_a_guest_sets_the_last_update_field() {
        Guest guest = new Guest("", "", "");

        new Audit().setAuditFields(guest);

        assertThat(guest.getLastUpdate()).isNotNull();
    }

    @Test
    public void a_guest_can_be_persisted() {
        Guest guest = new Guest("foo", "bar", "qux");

        new Audit().setAuditFields(guest);

        getEntityManager().persist(guest);

        getEntityManager().flush();
    }

    @Test
    public void a_guest_can_be_transformed_to_xml() {
        StringWriter w = new StringWriter();
        Guest guest = new Guest("foo", "bar", "qux");
        guest.setLastUpdate(now());
        JAXB.marshal(guest, w);
        assertThat(w.toString()).startsWith("<?xml");


        System.err.println(guest + " -> " + w.toString());
    }

}
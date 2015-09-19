package org.koenighotze.jee7hotel.domain;

import org.junit.Test;
import org.koenighotze.jee7hotel.business.AbstractBasePersistenceTest;
import org.koenighotze.jee7hotel.framework.persistence.audit.Audit;

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
}
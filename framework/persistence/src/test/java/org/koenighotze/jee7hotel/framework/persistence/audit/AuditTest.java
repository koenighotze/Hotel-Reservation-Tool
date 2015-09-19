package org.koenighotze.jee7hotel.framework.persistence.audit;

import org.junit.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

import static java.time.LocalDateTime.now;
import static org.fest.assertions.Assertions.assertThat;

/**
 * @author dschmitz
 */
public class AuditTest {

    private static class NonAuditableClass {
        private LocalDateTime lastUpdate;
    }

    private static class AuditableClass {
        @Auditable
        private Date lastUpdate;
    }

    @Test
    public void the_audit_fields_are_set_if_marked_as_auditable() throws InterruptedException {
        final Timestamp NOW =  Timestamp.from(Instant.now());

        AuditableClass auditable = new AuditableClass();
        new Audit().setAuditFields(auditable);

        assertThat(auditable.lastUpdate).isNotNull();

        assertThat(auditable.lastUpdate.getTime()).isGreaterThan(NOW.getTime());
    }

    @Test
    public void the_audit_skips_fields_that_are_not_auditable() {
        final LocalDateTime NOW = now();

        NonAuditableClass notAuditable = new NonAuditableClass();
        notAuditable.lastUpdate = NOW;
        new Audit().setAuditFields(notAuditable);

        assertThat(notAuditable.lastUpdate).isNotNull();
        assertThat(notAuditable.lastUpdate).isEqualTo(NOW);
    }

    @Test
    public void the_audit_continues_silently_if_the_fields_are_missing() {
        new Audit().setAuditFields(""); // no exceptions...only silence
    }
}
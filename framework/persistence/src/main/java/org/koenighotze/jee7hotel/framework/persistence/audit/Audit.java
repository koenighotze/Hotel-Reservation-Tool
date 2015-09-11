package org.koenighotze.jee7hotel.framework.persistence.audit;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.logging.Logger;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;

/**
 * @author dschmitz
 */
public class Audit {
    private static final Logger LOGGER = Logger.getLogger(Audit.class.getName());

    @PreUpdate
    @PrePersist
    public void setAuditFields(Object entity) {
        final LocalDateTime NOW = now();

        for (Field field : entity.getClass().getDeclaredFields()) {
            if (field.getAnnotation(Auditable.class) != null && field.getType().equals(Date.class)) {
                try {
                    LOGGER.finest(() -> format("Setting %s field %s to %s", entity.getClass(), field.getName(), NOW));
                    field.setAccessible(true);
                    field.set(entity, java.sql.Timestamp.valueOf(NOW));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

package org.koenighotze.jee7hotel.framework.persistence.audit;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author dschmitz
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface Auditable {
}

package org.koenighotze.jee7hotel.business.eventsource;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

/**
 * @author koenighotze
 */

@InterceptorBinding
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE, METHOD})
public @interface EventSource {

}

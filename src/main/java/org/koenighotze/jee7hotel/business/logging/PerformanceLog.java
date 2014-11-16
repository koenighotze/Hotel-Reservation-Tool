package org.koenighotze.jee7hotel.business.logging;

import javax.inject.Qualifier;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
/**
 * Created by dschmitz on 14.11.14.
 */


@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE})
@Qualifier
public @interface PerformanceLog {
}

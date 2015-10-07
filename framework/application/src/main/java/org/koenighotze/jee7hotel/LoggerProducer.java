

package org.koenighotze.jee7hotel;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Named;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

/**
 * Producer for java.util.logging Logger instances.
 *
 * @author dschmitz
 */
@Named
@ApplicationScoped
public class LoggerProducer {
    public @Produces Logger createLogger(InjectionPoint injectionPoint) {
      return getLogger(injectionPoint.getMember().getDeclaringClass().getName());
   }

}

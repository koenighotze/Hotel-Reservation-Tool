

package org.koenighotze.jee7hotel.business;

import org.koenighotze.jee7hotel.business.events.NewReservationEvent;
import org.koenighotze.jee7hotel.business.eventsource.EventSourceInterceptor;
import org.koenighotze.jee7hotel.business.logging.PerformanceLogger;

import javax.ejb.Asynchronous;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Async handler for reservations. 
 * Basically sets the state to confirmed after up to 10 sec.
 *
 *
 * @author dschmitz
 */
@Named
@Singleton
@Interceptors({
        PerformanceLogger.class
})
public class ReservationBackendHandler {
    private static final Logger LOGGER = Logger.getLogger(ReservationBackendHandler.class.getName()); 
    
    @Inject
    private BookingService bookingService;

    private final Random rand = new Random();
    
    @Asynchronous
    public void handleNewReservations(@Observes NewReservationEvent newReservation) {
        int duration = rand.nextInt(6) + 5;
        LOGGER.log(Level.INFO, "Incoming new reservation...{0}", newReservation); 

        try {
            LOGGER.log(Level.INFO, "delaying {0} secs before accepting", duration);
            Thread.sleep(duration * 1000L);

            this.bookingService.confirmReservation(newReservation.getReservationNumber());

            LOGGER.log(Level.INFO, "Sending Email to guest about confirmation for {0}",
                    newReservation.getReservationNumber());
        } catch (InterruptedException ex) {
            LOGGER.log(Level.SEVERE, "Cannot sleep", ex);
            Thread.currentThread().interrupt();
        }
    }
}

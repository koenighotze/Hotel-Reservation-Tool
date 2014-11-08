

package org.koenighotze.jee7hotel.business;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Async handler for reservations. 
 * Basically sets the state to confirmed after up to 10 sec.
 *
 *
 * @author dschmitz
 */
@Named
@Singleton
public class ReservationBackendHandler {
    private static final Logger LOGGER = Logger.getLogger(ReservationBackendHandler.class.getName()); 
    
    @Inject
    private BookingService bookingService;
            
    
    private final Random rand = new Random();
    
    @Asynchronous
    public void handleNewReservations(@Observes NewReservationEvent newReservation) {
        int duration = rand.nextInt(6) + 5;
        LOGGER.log(Level.INFO, "Incoming new reservation...{0}", newReservation); 
        LOGGER.log(Level.INFO, "delaying {0} secs before accepting", duration);
        
        try {
            Thread.sleep(duration * 1000L);
        } catch (InterruptedException ex) {
            LOGGER.log(Level.SEVERE, "Cannot sleep", ex);
            Thread.currentThread().interrupt();
        }

        this.bookingService.confirmReservation(newReservation.getReservationNumber());
        
        LOGGER.log(Level.INFO, "Sending Email to guest about confirmation for {0}", 
                newReservation.getReservationNumber());
    }
}



package org.koenighotze.jee7hotel.business;

/**
 *
 * @author dschmitz
 */
public class NewReservationEvent {
    private final String reservationNumber;

    public NewReservationEvent(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }
    
    public String getReservationNumber() {
        return this.reservationNumber;
    }

}
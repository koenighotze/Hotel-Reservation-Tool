

package org.koenighotze.jee7hotel.frontend;

import org.koenighotze.jee7hotel.business.BookingService;
import org.koenighotze.jee7hotel.domain.Reservation;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 *
 * @author dschmitz
 */
@Named
@ViewScoped
public class BookingDetailsBean implements Serializable {
    private String reservationNumber;
        
    private Reservation reservation;
    
    @Inject
    private BookingService bookingService;
    
    @PostConstruct
    public void init() {
        this.reservation = null;
        if (this.reservationNumber == null) {
            return;
        }
        
        this.bookingService.findReservationByNumber(this.reservationNumber)
                .ifPresent(r -> this.reservation = r);
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
    
    public void reopen() {
        this.bookingService.reopenReservation(this.reservation.getReservationNumber());
        
        FacesMessageHelper.addMessage(null, FacesMessage.SEVERITY_INFO, 
                    "Reservation " + this.reservationNumber + " is reopened!", "");
    }
    
    
    public void cancel() {
        this.bookingService.cancelReservation(this.reservation.getReservationNumber());
        
        FacesMessageHelper.addMessage(null, FacesMessage.SEVERITY_INFO, 
                    "Reservation " + this.reservationNumber + " is canceled!", "");
    }
}



package demo.frontend;

import demo.business.BookingService;
import demo.domain.Reservation;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

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
        
        this.reservation = this.bookingService.findReservationByNumber(this.reservationNumber);
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
        this.bookingService.reopenReservation(this.reservation);
        
        FacesMessageHelper.addMessage(null, FacesMessage.SEVERITY_INFO, 
                    "Reservation " + this.reservationNumber + " is reopened!", "");
    }
    
    
    public void cancel() {
        this.bookingService.cancelReservation(this.reservation);
        
        FacesMessageHelper.addMessage(null, FacesMessage.SEVERITY_INFO, 
                    "Reservation " + this.reservationNumber + " is canceled!", "");
    }
}

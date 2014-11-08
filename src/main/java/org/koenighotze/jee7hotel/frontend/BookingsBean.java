

package org.koenighotze.jee7hotel.frontend;

import org.koenighotze.jee7hotel.business.BookingService;
import org.koenighotze.jee7hotel.domain.Reservation;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author dschmitz
 */
@Named
@RequestScoped
public class BookingsBean {
    @Inject
    private BookingService bookingService;
    
    private List<Reservation> reservations;


    public void setBookings(List<Reservation> reservations) {
        this.reservations = reservations;
    }
    
    
    @PostConstruct
    public void init() {
        this.reservations = this.bookingService.getAllReservations();
    }
    
    public List<Reservation> getBookings() {
        return this.reservations;
    }
}

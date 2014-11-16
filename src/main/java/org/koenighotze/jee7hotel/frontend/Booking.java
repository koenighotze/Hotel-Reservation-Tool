

package org.koenighotze.jee7hotel.frontend;

import org.koenighotze.jee7hotel.domain.Guest;
import org.koenighotze.jee7hotel.domain.Room;

import java.time.LocalDate;

/**
 *
 * @author dschmitz
 */
public class Booking {
    private Guest guest;
   
    private Room room;
    
    private LocalDate checkinDate;

    private LocalDate checkoutDate;

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    
    
    public LocalDate getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(LocalDate checkinDate) {
        this.checkinDate = checkinDate;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }
    
    
    
}

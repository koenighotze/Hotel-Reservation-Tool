

package org.koenighotze.jee7hotel.frontend;

import org.koenighotze.jee7hotel.domain.Guest;
import org.koenighotze.jee7hotel.domain.Room;
import java.util.Date;

/**
 *
 * @author dschmitz
 */
public class Booking {
    private Guest guest;
   
    private Room room;
    
    private Date checkinDate;

    private Date checkoutDate;

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

    
    
    public Date getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(Date checkinDate) {
        this.checkinDate = checkinDate;
    }

    public Date getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(Date checkoutDate) {
        this.checkoutDate = checkoutDate;
    }
    
    
    
}



package org.koenighotze.jee7hotel.booking.frontend.model;

import java.time.LocalDate;

/**
 * @author dschmitz
 */
public class Booking {
    private String guest;

    private String room;

    private LocalDate checkinDate;

    private LocalDate checkoutDate;

    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
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

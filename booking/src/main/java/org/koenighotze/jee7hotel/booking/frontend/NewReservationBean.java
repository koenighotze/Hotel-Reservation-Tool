package org.koenighotze.jee7hotel.booking.frontend;

import org.koenighotze.jee7hotel.booking.business.BookingService;
import org.koenighotze.jee7hotel.booking.domain.Reservation;
import org.koenighotze.jee7hotel.booking.frontend.model.Booking;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static java.lang.String.format;
import static java.time.LocalDate.now;

/**
 * Handles the creation of new reservations.
 *
 * @author dschmitz
 */
@Named
@ViewScoped
public class NewReservationBean implements Serializable {
    private String guestId;

    private String roomId;

    private Booking booking = new Booking();

    @Inject
    private BookingService bookingService;

    public Booking getBooking() {
        return this.booking;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(@NotNull String guestId) {
        this.guestId = guestId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(@NotNull String roomId) {
        this.roomId = roomId;
    }

    public void init(ComponentSystemEvent evt) {
        this.booking = new Booking();
        this.booking.setGuest(guestId);
        this.booking.setRoom(roomId);
        this.booking.setCheckinDate(now().plusDays(1));
        this.booking.setCheckoutDate(now().plusDays(2));
    }

    public void setRoomNumber(@NotNull String number) {
        this.roomId = number;
        this.booking.setRoom(number);
    }

    public String getRoomNumber() {
        if (null == this.booking.getRoom()) {
            return null;
        }

        return this.booking.getRoom();
    }

    public String addReservation() {
        Reservation realReservation
                = this.bookingService.bookRoom(this.booking.getGuest(), this.booking.getRoom(),
                this.booking.getCheckinDate(), this.booking.getCheckoutDate());

        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.setKeepMessages(true);

        FacesMessage message = new FacesMessage("Room "
                + this.booking.getRoom()
                + " booked for "
                + this.booking.getGuest()
                + "; Reservation number "
                + realReservation.getReservationNumber()
                + " Costs: "
                + realReservation.getCostsInEuro() + " EUR");
        FacesContext.getCurrentInstance().addMessage(null, message);
        this.booking = null;

        return format("/booking/booking.xhtml?reservationNumber=%s&faces-redirect=true", realReservation.getReservationNumber());
    }
}

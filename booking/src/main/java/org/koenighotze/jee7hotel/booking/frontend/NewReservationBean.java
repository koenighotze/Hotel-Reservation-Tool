package org.koenighotze.jee7hotel.booking.frontend;

import org.apache.commons.lang3.StringUtils;
import org.koenighotze.jee7hotel.booking.business.BookingService;
import org.koenighotze.jee7hotel.booking.domain.Reservation;
import org.koenighotze.jee7hotel.booking.frontend.model.Booking;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.io.IOException;
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
    private String publicGuestId;

    private String backlink;

    private String roomId;

    private Booking booking = new Booking();

    @Inject
    private BookingService bookingService;



    public Booking getBooking() {
        return this.booking;
    }

    public String getPublicGuestId() {
        return publicGuestId;
    }

    public void setPublicGuestId(@NotNull String publicGuestId) {
        this.publicGuestId = publicGuestId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(@NotNull String roomId) {
        this.roomId = roomId;
    }

    public void init(ComponentSystemEvent evt) {
        this.booking = new Booking();
        this.booking.setGuest(publicGuestId);
        this.booking.setRoom(roomId);
        this.booking.setCheckinDate(now().plusDays(1));
        this.booking.setCheckoutDate(now().plusDays(2));
    }

    public String getRoomNumber() {
        if (null == this.booking.getRoom()) {
            return null;
        }

        return this.booking.getRoom();
    }

    public void setRoomNumber(@NotNull String number) {
        this.roomId = number;
        this.booking.setRoom(number);
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

        if (StringUtils.isBlank(backlink)) {
            return format("/booking.xhtml?reservationNumber=%s&faces-redirect=true", realReservation.getReservationNumber());
        }
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            externalContext.redirect(backlink);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FacesContext.getCurrentInstance().responseComplete();
        return null;
    }

    public String getBacklink() {
        return backlink;
    }

    public void setBacklink(String backlink) {
        this.backlink = backlink;
    }
}

package org.koenighotze.jee7hotel.booking.frontend;

import org.apache.commons.lang3.StringUtils;
import org.koenighotze.jee7hotel.booking.business.BookingService;
import org.koenighotze.jee7hotel.booking.domain.Reservation;
import org.koenighotze.jee7hotel.booking.frontend.model.Booking;
import org.koenighotze.jee7hotel.booking.persistence.GuestModelRepository;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
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
import static javax.faces.context.FacesContext.getCurrentInstance;

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

    @Inject
    private GuestModelRepository guestModelRepository;

    public Booking getBooking() {
        return booking;
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
        booking = new Booking();
        booking.setGuest(publicGuestId);
        booking.setRoom(roomId);
        booking.setCheckinDate(now().plusDays(1));
        booking.setCheckoutDate(now().plusDays(2));
    }

    public String getRoomNumber() {
        if (null == booking.getRoom()) {
            return null;
        }

        return booking.getRoom();
    }

    public void setRoomNumber(@NotNull String number) {
        roomId = number;
        booking.setRoom(number);
    }

    public String addReservation() {
        Flash flash = getCurrentInstance().getExternalContext().getFlash();
        flash.setKeepMessages(true);

        if (!guestModelRepository.findByPublicId(booking.getGuest()).isPresent()) {
            FacesMessage message = new FacesMessage(format("Guest %s is currently unknown. Booking will be created but check in Guest system for correctness", booking.getGuest()));
            message.setSeverity(FacesMessage.SEVERITY_WARN);
            getCurrentInstance().addMessage(null, message);
        }

        Reservation realReservation
                = bookingService.bookRoom(booking.getGuest(), booking.getRoom(),
                booking.getCheckinDate(), booking.getCheckoutDate());

        FacesMessage message = new FacesMessage(format("Room %s is booked for %s; Reservation number %s; Cost: %s EUR",
                booking.getRoom(), booking.getGuest(), realReservation.getReservationNumber(),
                realReservation.getCostsInEuro()));
        getCurrentInstance().addMessage(null, message);
        booking = null;

        if (StringUtils.isBlank(backlink)) {
            return format("/booking.xhtml?reservationNumber=%s&faces-redirect=true", realReservation.getReservationNumber());
        }

        ExternalContext externalContext = getCurrentInstance().getExternalContext();
        try {
            externalContext.redirect(backlink);
        } catch (IOException e) {
            // TODO: handle exception
        }
        getCurrentInstance().responseComplete();
        return null;
    }

    public String getBacklink() {
        return backlink;
    }

    public void setBacklink(String backlink) {
        this.backlink = backlink;
    }
}

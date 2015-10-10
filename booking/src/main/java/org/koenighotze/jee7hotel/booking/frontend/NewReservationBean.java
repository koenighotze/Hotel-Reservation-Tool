package org.koenighotze.jee7hotel.booking.frontend;

import org.koenighotze.jee7hotel.booking.business.BookingService;
import org.koenighotze.jee7hotel.booking.domain.Reservation;
import org.koenighotze.jee7hotel.booking.frontend.model.Booking;
import org.koenighotze.jee7hotel.booking.persistence.GuestModelRepository;

import javax.annotation.PostConstruct;
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
import static javax.faces.application.FacesMessage.SEVERITY_WARN;
import static javax.faces.context.FacesContext.getCurrentInstance;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Handles the creation of new reservations.
 *
 * @author dschmitz
 */
@Named
@ViewScoped
public class NewReservationBean implements Serializable {

    private String backlink;

    private Booking booking = new Booking();

    @Inject
    private BookingService bookingService;

    @Inject
    private GuestModelRepository guestModelRepository;

    @PostConstruct
    public void initBooking() {
        booking = new Booking();
        booking.setCheckinDate(now().plusDays(1));
        booking.setCheckoutDate(now().plusDays(2));
    }

    public Booking getBooking() {
        return booking;
    }

    public String getPublicGuestId() {
        return booking.getGuest();
    }

    public void setPublicGuestId(@NotNull String publicGuestId) {
        booking.setGuest(publicGuestId);
    }

    public void init(ComponentSystemEvent evt) {
//        booking = new Booking();
//        booking.setCheckinDate(now().plusDays(1));
//        booking.setCheckoutDate(now().plusDays(2));
    }

    public String getRoomNumber() {
        return booking.getRoom();
    }

    public void setRoomNumber(@NotNull String number) {
        booking.setRoom(number);
    }

    public String addReservation() {
        if (!guestModelRepository.findByPublicId(booking.getGuest()).isPresent()) {
            FacesMessage message = new FacesMessage(format("Guest %s is currently unknown. Booking will be created but check in Guest system for correctness", booking.getGuest()));
            message.setSeverity(SEVERITY_WARN);
            getCurrentInstance().addMessage(null, message);
        }

        // TODO should do the same with room numbers

        Reservation realReservation
                = bookingService.bookRoom(booking.getGuest(), booking.getRoom(),
                booking.getCheckinDate(), booking.getCheckoutDate());
        // TODO handle errors

        if (isBlank(backlink)) {
            return navigateToBookingsPage(realReservation);
        }

        return navigateToBacklink(realReservation);
    }

    protected String navigateToBacklink(Reservation realReservation) {
        ExternalContext externalContext = getCurrentInstance().getExternalContext();
        try {
            String requestParams = format("?reservationNumber=%s&publicGuestId=%s&roomNumber=%s", realReservation.getReservationNumber(), realReservation.getGuest(), realReservation.getAssignedRoom());

            externalContext.redirect(backlink + requestParams);
        } catch (IOException e) {
            // TODO: handle exception
        }
        getCurrentInstance().responseComplete();
        return null;
    }

    protected String navigateToBookingsPage(Reservation realReservation) {
        Flash flash = getCurrentInstance().getExternalContext().getFlash();
        FacesMessage message = new FacesMessage(format("Room %s is booked for %s; Reservation number %s; Cost: %s EUR",
                booking.getRoom(), booking.getGuest(), realReservation.getReservationNumber(),
                realReservation.getCostsInEuro()));
        getCurrentInstance().addMessage(null, message);
        booking = null;
        flash.setKeepMessages(true); // only keep messages if we do not have a backlink
        return format("/booking.xhtml?reservationNumber=%s&faces-redirect=true", realReservation.getReservationNumber());
    }

    public String getBacklink() {
        return backlink;
    }

    public void setBacklink(String backlink) {
        this.backlink = backlink;
    }
}

package org.koenighotze.jee7hotel.guest.frontend;

import org.koenighotze.jee7hotel.framework.application.registry.AddReservationUrl;
import org.koenighotze.jee7hotel.guest.business.GuestService;
import org.koenighotze.jee7hotel.guest.domain.Guest;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

import static java.lang.String.format;
import static javax.faces.application.FacesMessage.SEVERITY_WARN;
import static javax.faces.context.FacesContext.getCurrentInstance;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.koenighotze.jee7hotel.framework.frontend.FacesMessageHelper.addFlashMessage;

/**
 * @author dschmitz
 */
@Named
@RequestScoped
public class GuestbookBean {
    @Inject
    private GuestService guestService;

    private List<Guest> guestList;

    private String publicGuestId;

    private String reservationNumber;

    private String roomNumber;

    @Inject
    @AddReservationUrl
    private String addNewReservationUrl;

    public void fillInitialGuestBook() {
        // TODO trigger batch via JMS?
        addFlashMessage(SEVERITY_WARN, "Guest import via Batch is still work in progress...");
    }

    public List<Guest> getGuestList() {
        return this.guestList;
    }

    public void setGuestList(List<Guest> guestList) {
        this.guestList = guestList;
    }

    @PostConstruct
    public void init() {
        this.guestList = this.guestService.getAllGuests();
    }

    /**
     * Adds a message if a reservation is indicated via get parameters
     *
     * @param evt the event
     */
    public void initReservationMessage(ComponentSystemEvent evt) {
        if (isBlank(publicGuestId) || isBlank(reservationNumber)) {
            return;
        }
        FacesMessage message = new FacesMessage(format("Room %s is booked for %s; Reservation number %s",
                roomNumber, publicGuestId, reservationNumber));
        getCurrentInstance().addMessage(null, message);
    }

    public String getPublicGuestId() {
        return publicGuestId;
    }

    public void setPublicGuestId(String publicGuestId) {
        this.publicGuestId = publicGuestId;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getAddNewReservationUrl() {
        return addNewReservationUrl;
    }
}

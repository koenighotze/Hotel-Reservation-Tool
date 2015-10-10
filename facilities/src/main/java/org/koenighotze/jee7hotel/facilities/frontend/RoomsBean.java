

package org.koenighotze.jee7hotel.facilities.frontend;

import org.koenighotze.jee7hotel.facilities.business.RoomService;
import org.koenighotze.jee7hotel.facilities.domain.Room;
import org.koenighotze.jee7hotel.framework.application.registry.AddReservationUrl;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import java.util.List;

import static java.lang.String.format;
import static javax.faces.application.FacesMessage.SEVERITY_WARN;
import static javax.faces.context.FacesContext.getCurrentInstance;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.koenighotze.jee7hotel.framework.frontend.FacesMessageHelper.addMessage;

/**
 * @author dschmitz
 */
// stereotype example -> effectively RequestScope and Named
@Model
public class RoomsBean {
    @Inject
    private RoomService roomService;

    private List<Room> rooms;

    @Inject
    @AddReservationUrl
    private String addNewReservationUrl;
    private String publicGuestId;
    private String reservationNumber;
    private String roomNumber;


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

    @PostConstruct
    public void init() {
        this.rooms = this.roomService.getAllRooms();

        if (this.rooms.isEmpty()) {
            addMessage(SEVERITY_WARN, "No rooms found!");
        }
    }

    public List<Room> getRooms() {
        return this.rooms;
    }

    public String getAddNewReservationUrl() {
        return addNewReservationUrl;
    }

    public void setPublicGuestId(String publicGuestId) {
        this.publicGuestId = publicGuestId;
    }

    public String getPublicGuestId() {
        return publicGuestId;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }
}

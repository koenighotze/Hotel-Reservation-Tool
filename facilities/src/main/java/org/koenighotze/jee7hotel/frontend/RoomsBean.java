

package org.koenighotze.jee7hotel.frontend;

import org.koenighotze.jee7hotel.business.RoomService;
import org.koenighotze.jee7hotel.domain.Room;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.util.List;

import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static org.koenighotze.jee7hotel.frontend.FacesMessageHelper.addMessage;

/**
 * @author dschmitz
 */
// stereotype example -> effectively RequestScope and Named
@Model
public class RoomsBean {
    @Inject
    private RoomService roomService;
    private List<Room> rooms;

    private Room selectedRoom;

    public Room getSelectedRoom() {
        return selectedRoom;
    }

    public void setSelectedRoom(Room selectedRoom) {
        this.selectedRoom = selectedRoom;
    }

    @PostConstruct
    public void init() {
        this.rooms = this.roomService.getAllRooms();

        if (this.rooms.isEmpty()) {
            addMessage(SEVERITY_ERROR,
                    "No rooms found!");
        }
    }

    public String showRoomDetails() {
        return null;
    }

    public String newReservation() {
        return null;
    }

    public List<Room> getRooms() {
        return this.rooms;
    }
}

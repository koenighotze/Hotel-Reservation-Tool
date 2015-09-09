package org.koenighotze.jee7hotel.frontend;

import org.koenighotze.jee7hotel.business.RoomService;
import org.koenighotze.jee7hotel.domain.Room;

import javax.enterprise.context.RequestScoped;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.inject.Named;

import static javax.faces.application.FacesMessage.SEVERITY_WARN;
import static org.koenighotze.jee7hotel.frontend.FacesMessageHelper.addMessage;

/**
 * @author koenighotze
 */
@Named
@RequestScoped
public class RoomDetailsBean {
    private Long roomId;

    @Inject
    private RoomService service;
    private Room room;

    public void loadRoom(ComponentSystemEvent evt) {
        if (null == this.roomId) {
            this.room = Room.nullValue();
            addMessage(SEVERITY_WARN, "No room id provided!");
            return;
        }

        if (null == this.room) {
            this.room = this.service.findRoomById(this.roomId)
                    .orElseGet(() -> {
                        addMessage(SEVERITY_WARN, "Cannot find room " + this.roomId);
                        return Room.nullValue();
                    });
        }
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}

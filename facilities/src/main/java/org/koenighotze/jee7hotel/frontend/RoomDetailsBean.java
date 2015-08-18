package org.koenighotze.jee7hotel.frontend;

import org.koenighotze.jee7hotel.business.RoomService;
import org.koenighotze.jee7hotel.domain.Room;
import org.koenighotze.jee7hotel.domain.RoomEquipment;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by dschmitz on 10.12.14.
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
            this.room = new Room("", RoomEquipment.BUDGET);
            FacesMessageHelper.addMessage(FacesMessage.SEVERITY_ERROR,
                    "No room id provided!");
            return;
        }
        if (null == this.room) {
            this.room = this.service.findRoomById(this.roomId)
                    .orElseGet(() -> {
                        FacesMessageHelper.addMessage(FacesMessage.SEVERITY_ERROR,
                                "Cannot find room " + this.roomId);
                        return new Room("", RoomEquipment.BUDGET);
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

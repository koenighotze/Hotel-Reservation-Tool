

package org.koenighotze.jee7hotel.domain;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

/**
 * Represents a single room.
 * @author dschmitz
 */
@Entity
public class Room {    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    

    @Version
    private Long version;
    
    @NotNull
    private String roomNumber;
    
    @NotNull
//    @Enumerated(EnumType.STRING)
    @Convert(converter = RoomEquipmentConverter.class)
    private RoomEquipment roomEquipment;
    
    
    Room() {
    }
    
    public Room(String roomNumber, RoomEquipment roomEquipment) {
        this.roomNumber = roomNumber;
        this.roomEquipment = roomEquipment;
    }

    public Long getId() {
        return id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public RoomEquipment getRoomEquipment() {
        return roomEquipment;
    }

    public Long getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "Room{" + "id=" + id + ", version=" + version + ", roomNumber=" + roomNumber + ", roomEquipment=" + roomEquipment + '}';
    }
    
    
    
}



package org.koenighotze.jee7hotel.frontend;

import org.koenighotze.jee7hotel.business.RoomService;
import org.koenighotze.jee7hotel.domain.Room;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import java.util.List;

import static org.koenighotze.jee7hotel.frontend.FacesMessageHelper.addMessage;
/**
 *
 * @author dschmitz
 */
// stereotype example -> effectively RequestScope and Named
@Model
public class RoomsBean {
    @Inject
    private RoomService roomService;
    private List<Room> rooms;
    
    @PostConstruct
    public void init() {
        this.rooms = this.roomService.getAllRooms();
        
        if (this.rooms.isEmpty()) {
            addMessage(FacesMessage.SEVERITY_ERROR,
                        "No rooms found!");
        }
    }
    
    public List<Room> getRooms() {
        return this.rooms;
    }
}

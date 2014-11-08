

package org.koenighotze.jee7hotel.frontend;

import org.koenighotze.jee7hotel.business.RoomService;
import org.koenighotze.jee7hotel.domain.Room;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author dschmitz
 */
@Named
@RequestScoped
public class RoomsBean {
    @Inject
    private RoomService roomService;
    private List<Room> rooms;
    
    @PostConstruct
    public void init() {
        this.rooms = this.roomService.getAllRooms();
        
        if (this.rooms.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "No rooms found!", null));
        }
    }
    
    public List<Room> getRooms() {
        return this.rooms;
    }
}

package org.koenighotze.jee7hotel.frontend;

import org.koenighotze.jee7hotel.business.GuestService;
import org.koenighotze.jee7hotel.domain.Guest;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.util.List;

/**
 *
 * @author dschmitz
 */
@Model
public class GuestbookBean {
    @Inject
    private GuestService guestService;
    private List<Guest> guestList;

    public void fillInitialGuestBook() {
        // TODO trigger batch via JMS?
    }
    
    public List<Guest> getGuestList() {
        return guestList;
    }

    public void setGuestList(List<Guest> guestList) {
        this.guestList = guestList;
    }
    
    @PostConstruct
    public void init() {
        this.guestList = this.guestService.getAllGuests();
    }    
}

package demo.frontend;

import demo.business.GuestService;
import demo.domain.Guest;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author dschmitz
 */
@Named
@RequestScoped
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
        this.guestList = this.guestService.findAllGuests();
    }    
}

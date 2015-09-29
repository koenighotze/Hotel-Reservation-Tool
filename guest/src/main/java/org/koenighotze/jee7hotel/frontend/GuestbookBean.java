package org.koenighotze.jee7hotel.frontend;

import org.koenighotze.jee7hotel.business.GuestService;
import org.koenighotze.jee7hotel.domain.Guest;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

import static javax.faces.application.FacesMessage.SEVERITY_WARN;
import static org.koenighotze.jee7hotel.frontend.FacesMessageHelper.addFlashMessage;

/**
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
}

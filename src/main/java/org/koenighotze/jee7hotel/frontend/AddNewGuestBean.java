package org.koenighotze.jee7hotel.frontend;

import org.koenighotze.jee7hotel.business.GuestService;
import org.koenighotze.jee7hotel.domain.Guest;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

// DO NOT USE: import javax.faces.bean.ViewScoped;

/**
 * @author dschmitz
 */
@Named
@ViewScoped
// @RolesAllowed({ "ADMIN", "CLERK" })
public class AddNewGuestBean implements Serializable {
    @NotNull
    private Guest guest;

    @Inject
    private GuestService guestService;

    @PostConstruct
    public void init() {
        this.guest = new Guest("", "");
    }

    public Guest getGuest() {
        return this.guest;
    }

    public void saveGuest() {
        this.guestService.saveGuest(this.guest);
        FacesMessage message = new FacesMessage("Guest saved: " + this.guest, "Guest saved:" + this.guest);
        FacesContext.getCurrentInstance().addMessage(null, message);
        this.guest = new Guest("", "");
    }
}

package org.koenighotze.jee7hotel.frontend;

import org.koenighotze.jee7hotel.business.GuestService;
import org.koenighotze.jee7hotel.domain.Guest;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Optional;

import static org.koenighotze.jee7hotel.frontend.FacesMessageHelper.addMessage;
/**
 *
 * @author dschmitz
 */
@Named
@ViewScoped
public class GuestDetailsBean implements Serializable {

    private Guest guest;

    private Long guestId;

    @Inject
    private GuestService service;

    public void loadGuest(ComponentSystemEvent evt) {
        if (null == this.guestId) {
            this.guest = new Guest("", "");
            addMessage(FacesMessage.SEVERITY_ERROR,
                    "No guest id provided!");
            return;
        }
        if (null == this.guest) {
            this.guest = this.service.findById(this.guestId)
                    .orElseGet(() -> {
                        addMessage(FacesMessage.SEVERITY_ERROR,
                                "Cannot find guest " + this.guestId);
                        return new Guest("", "");
                    });
        }
    }
    
    public GuestDetailsBean() {
    }

    public Long getGuestId() {
        return guestId;
    }

    public void setGuestId(Long guestId) {
        this.guestId = guestId;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public void saveChanges() {
        Optional<Guest> updated = this.service.updateGuestDetails(this.guest);
        if (!updated.isPresent()) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot update guest " + this.guest, null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        addMessage(FacesMessage.SEVERITY_INFO, "Guest updated: " + updated.get());

        this.guest = null;
    }
}

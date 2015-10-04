package org.koenighotze.jee7hotel.guest.frontend;

import org.koenighotze.jee7hotel.guest.business.GuestService;
import org.koenighotze.jee7hotel.guest.domain.Guest;

import javax.faces.event.ComponentSystemEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Optional;

import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;
import static org.koenighotze.jee7hotel.framework.frontend.FacesMessageHelper.addMessage;

/**
 * @author dschmitz
 */
@Named
@ViewScoped
public class GuestDetailsBean implements Serializable {
    private Guest guest;
    private String publicGuestId;

    @Inject
    private GuestService service;

    public GuestDetailsBean() {
    }

    public void loadGuest(ComponentSystemEvent evt) {
        if (null == this.publicGuestId) {
            this.guest = Guest.nullGuest();
            addMessage(SEVERITY_ERROR,
                    "No guest id provided!");
            return;
        }
        if (null == this.guest) {
            this.guest = this.service.findByPublicId(this.publicGuestId)
                    .orElseGet(() -> {
                        addMessage(SEVERITY_ERROR,
                                "Cannot find guest " + this.publicGuestId);
                        return Guest.nullGuest();
                    });
        }
    }

    public String getPublicGuestId() {
        return publicGuestId;
    }

    public void setPublicGuestId(String publicGuestId) {
        this.publicGuestId = publicGuestId;
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
            addMessage(SEVERITY_ERROR, "Cannot update guest " + this.guest);
            return;
        }

        addMessage(SEVERITY_INFO, "Guest updated: " + updated.get());

        this.guest = null;
    }
}

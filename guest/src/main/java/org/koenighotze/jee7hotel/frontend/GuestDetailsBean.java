package org.koenighotze.jee7hotel.frontend;

import org.koenighotze.jee7hotel.business.GuestService;
import org.koenighotze.jee7hotel.domain.Guest;

import javax.faces.event.ComponentSystemEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;

import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;
import static org.koenighotze.jee7hotel.frontend.FacesMessageHelper.addMessage;

/**
 * @author dschmitz
 */
@Named
@ViewScoped
public class GuestDetailsBean {
    private Guest guest;

    private Long guestId;

    @Inject
    private GuestService service;

    public GuestDetailsBean() {
    }

    public void loadGuest(ComponentSystemEvent evt) {
        if (null == this.guestId) {
            this.guest = new Guest("", "");
            addMessage(SEVERITY_ERROR,
                    "No guest id provided!");
            return;
        }
        if (null == this.guest) {
            this.guest = this.service.findById(this.guestId)
                    .orElseGet(() -> {
                        addMessage(SEVERITY_ERROR,
                                "Cannot find guest " + this.guestId);
                        return new Guest("", "");
                    });
        }
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
            addMessage(SEVERITY_ERROR, "Cannot update guest " + this.guest);
            return;
        }

        addMessage(SEVERITY_INFO, "Guest updated: " + updated.get());

        this.guest = null;
    }
}

package demo.frontend;

import demo.business.GuestService;
import demo.domain.Guest;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

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
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "No guest id provided!", null));
            return;
        }
        if (null == this.guest) {
            this.guest = this.service.findById(this.guestId);
            if (null == this.guest) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                                "Cannot find guest " + this.guestId, null));
                
            }             
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
        Guest updated = this.service.updateGuestDetails(this.guest);
        if (null == updated) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot update guest " + this.guest, null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        FacesMessage message = new FacesMessage("Guest updated: " + this.guest);
        FacesContext.getCurrentInstance().addMessage(null, message);

        this.guest = null;
    }
}

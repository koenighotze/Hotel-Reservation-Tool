

package org.koenighotze.jee7hotel.frontend.addnewreservationflow;

import org.koenighotze.jee7hotel.business.GuestService;
import org.koenighotze.jee7hotel.domain.Guest;
import org.koenighotze.jee7hotel.domain.converter.LocalDateConverter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.flow.FlowScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author dschmitz
 */
@Named("addNewReservationWizardBean")
@FlowScoped("addnewreservationflow") 
public class AddNewReservationWizardBean implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(AddNewReservationWizardBean.class.getName());

    @Inject
    private GuestService guestService;

    private List<Guest> guestList;
    private LocalDate checkoutDate;
    private LocalDate checkinDate;


    @PostConstruct
    public void init() {
        this.guestList = this.guestService.getAllGuests();

    }


    public List<Guest> getGuestList() {
        return guestList;
    }


    public String confirmBooking() {
        LOGGER.info("Returning from flow");
        return "/booking/bookings.xhtml?faces-redirect=true";
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckinDate(LocalDate checkinDate) {
        this.checkinDate = checkinDate;
    }

    public void setCheckoutDate(LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public LocalDate getCheckinDate() {
        return checkinDate;
    }
}



package org.koenighotze.jee7hotel.frontend.addnewreservationflow;

import java.io.Serializable;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.flow.FlowScoped;
import javax.inject.Named;

/**
 *
 * @author dschmitz
 */
@Named
@FlowScoped("addnewreservationflow") 
public class AddNewReservationWizardBean implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(AddNewReservationWizardBean.class.getName());
    
    @PostConstruct
    public void init() {
        LOGGER.info("Created " + this);
    }
    
    
    public String returnValue() {
        LOGGER.info("Returning from flow");
        return "/booking/bookings.xhtml?faces-redirect=true";
    }
}

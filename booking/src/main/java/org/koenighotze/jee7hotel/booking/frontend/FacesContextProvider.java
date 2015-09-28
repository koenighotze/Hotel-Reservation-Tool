package org.koenighotze.jee7hotel.booking.frontend;

import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * @author dschmitz
 */
@Named
public class FacesContextProvider {
    // TODO refactor to @Produces
    public FacesContext getCurrentFacesContext() {
        return FacesContext.getCurrentInstance();
    }
}

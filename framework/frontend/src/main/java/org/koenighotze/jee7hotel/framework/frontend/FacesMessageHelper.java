

package org.koenighotze.jee7hotel.framework.frontend;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.Flash;
import javax.inject.Named;

import static javax.faces.application.FacesMessage.*;
import static javax.faces.context.FacesContext.getCurrentInstance;

/**
 *
 * @author dschmitz
 */

@Named
@ApplicationScoped
public class FacesMessageHelper {
    public static void addMessage(Severity severity, String summary) {
        addMessage(null, severity, summary, null);
    }
    public static void addMessage(String clientId, Severity severity, String summary, String details) {
        getCurrentInstance().addMessage(clientId,
                    new FacesMessage(severity,
                        summary, details));
    }

    public static void addFlashMessage(Severity severity, String summary) {
        Flash flash = getCurrentInstance().getExternalContext().getFlash();
        flash.setKeepMessages(true);
        FacesMessage message = new FacesMessage(severity, summary, summary);
        getCurrentInstance().addMessage(null, message);
    }

    public boolean isErrorMessage(FacesMessage message) {
        return message.getSeverity().equals(SEVERITY_ERROR);
    }
    
    public boolean isInfoMessage(FacesMessage message) {
        return message.getSeverity().equals(SEVERITY_INFO);
    }
    
    public String getStyleClassFor(FacesMessage message) {
        
        if (isInfoMessage(message)) {
            return "alert-success";
        }
        else if (isErrorMessage(message)) {
            return "alert-danger";
        }
        
        return "alert-info";
    }
}

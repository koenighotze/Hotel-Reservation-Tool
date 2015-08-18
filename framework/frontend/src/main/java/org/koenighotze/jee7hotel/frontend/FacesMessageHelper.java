

package org.koenighotze.jee7hotel.frontend;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Named;

/**
 *
 * @author dschmitz
 */

@Named
@ApplicationScoped
public class FacesMessageHelper {
    public static void addMessage(FacesMessage.Severity severity, String summary) {
        addMessage(null, severity, summary, null);
    }
    public static void addMessage(String clientId, FacesMessage.Severity severity, String summary, String details) {
        FacesContext.getCurrentInstance().addMessage(clientId,
                    new FacesMessage(severity,
                        summary, details));
    }

    public static void addFlashMessage(FacesMessage.Severity severity, String summary) {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.setKeepMessages(true);
        FacesMessage message = new FacesMessage(severity, summary, summary);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public boolean isErrorMessage(FacesMessage message) {
        return message.getSeverity().equals(FacesMessage.SEVERITY_ERROR);
    }
    
    public boolean isInfoMessage(FacesMessage message) {
        return message.getSeverity().equals(FacesMessage.SEVERITY_INFO);
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

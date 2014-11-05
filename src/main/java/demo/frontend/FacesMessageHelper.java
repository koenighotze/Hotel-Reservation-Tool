

package demo.frontend;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author dschmitz
 */

@Named
@ApplicationScoped
public class FacesMessageHelper {
    public static void addMessage(String clientId, FacesMessage.Severity severity, String summary, String details) {
        FacesContext.getCurrentInstance().addMessage(clientId,
                    new FacesMessage(severity,
                        summary, details));
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

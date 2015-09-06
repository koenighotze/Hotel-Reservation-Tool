package org.koenighotze.jee7hotel.frontend.component;

import javax.faces.application.FacesMessage;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author dschmitz
 */
@FacesComponent("org.koenighotze.jee7hotel.frontend.component.BootstrappedMessages")
public class BootstrappedMessages extends UINamingContainer {

    public List<FacesMessage> getFacesMessages() {
        List<FacesMessage> messageList = new ArrayList<>();

        Iterator<FacesMessage> messageItr = getFacesContext().getMessages(null);
        while (messageItr.hasNext()) {
            FacesMessage message = messageItr.next();
            messageList.add(message);
            messageItr.remove();
        }

        return messageList;
    }
}

package org.koenighotze.jee7hotel.frontend.component;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import org.junit.Test;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.LinkedList;
import java.util.List;

import static java.util.Collections.*;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;
import static javax.faces.context.FacesContext.getCurrentInstance;
import static org.fest.assertions.Assertions.assertThat;

/**
 * @author dschmitz
 */
public class BootstrappedMessagesTest {
    @Mocked
    private FacesContext facesContext = null;

    @Test
    public void the_message_list_contains_all_global_messages() {
        final FacesMessage message = new FacesMessage(SEVERITY_INFO, "", "");

        new Expectations() {
            {
                getCurrentInstance();
                result = facesContext;
            }

            {
                getCurrentInstance().getMessages(null);
                result = new LinkedList<>(singleton(message)).iterator();
            }
        };

        List<FacesMessage> facesMessages = new BootstrappedMessages().getFacesMessages();
        assertThat(facesMessages).isEqualTo(singletonList(message));
    }

    @Test
    public void the_message_list_does_not_contain_messages_that_belong_to_a_component() {
        final FacesMessage componentMessage = new FacesMessage(SEVERITY_INFO, "", "");

        new Expectations() {
            {
                getCurrentInstance();
                result = facesContext;
            }

            {
                getCurrentInstance().getMessages(null);
                result = new LinkedList<>().iterator();
            }
        };

        // add local message
        getCurrentInstance().addMessage("foo", componentMessage);

        List<FacesMessage> facesMessages = new BootstrappedMessages().getFacesMessages();
        assertThat(facesMessages).isEqualTo(emptyList());

        new Verifications() {
            {
                getCurrentInstance().getMessages("foo");
                times = 0;
            }
        };
    }
}
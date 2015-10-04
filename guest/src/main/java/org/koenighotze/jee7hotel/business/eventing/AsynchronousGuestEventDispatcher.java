package org.koenighotze.jee7hotel.business.eventing;

import org.koenighotze.jee7hotel.domain.Guest;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSContext;
import javax.jms.JMSRuntimeException;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.xml.bind.JAXB;
import java.io.StringWriter;
import java.util.logging.Logger;

import static java.lang.String.format;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

/**
 * @author dschmitz
 */
@Named
@ApplicationScoped
public class AsynchronousGuestEventDispatcher {
    private static final Logger LOGGER = Logger.getLogger(AsynchronousGuestEventDispatcher.class.getName());

    public static final String GUEST_EVENT_TOPIC = "java:global/jms/topic/guest";

    private JMSContext jmsContext;

    private Topic guestEventQueue;

    public AsynchronousGuestEventDispatcher() {
    }

    @Inject
    public AsynchronousGuestEventDispatcher(JMSContext jmsContext, @GuestEventTopic Topic guestEventQueue) {
        this.jmsContext = jmsContext;
        this.guestEventQueue = guestEventQueue;
    }

    public void distributeGuestEvent(@Observes @Background Guest guest) {
        LOGGER.info(() -> format("Handling event for %s", guest));
        sendGuestEvent(guest);
    }

    public void sendGuestEvent(Guest guest) {
        if (null == jmsContext || null == guestEventQueue) {
            LOGGER.log(WARNING, () -> "Sending messages is deactivated!");
            return;
        }

        LOGGER.info(() -> format("Sending info about %s to %s", guest, guestEventQueue));
        try {
            StringWriter w = new StringWriter();
            JAXB.marshal(guest, w);
            TextMessage textMessage = this.jmsContext.createTextMessage(w.toString());
            this.jmsContext.createProducer().send(this.guestEventQueue, textMessage);
        } catch (JMSRuntimeException e) {
            LOGGER.log(SEVERE, e, () -> "Cannot send message due to technical reasons!");
        }
    }
}

package org.koenighotze.jee7hotel.business;

import org.koenighotze.jee7hotel.domain.Guest;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.*;
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
@Stateless // JMS Resource Injection seems to need this
@JMSDestinationDefinitions({
        @JMSDestinationDefinition(
                name = AsynchronousGuestEventDispatcher.GUEST_EVENT_TOPIC,
                resourceAdapter = "jmsra",
                interfaceName = "javax.jms.Topic",
                destinationName = "guestEventTopic")
})
public class AsynchronousGuestEventDispatcher {
    private static final Logger LOGGER = Logger.getLogger(AsynchronousGuestEventDispatcher.class.getName());

    public static final String GUEST_EVENT_TOPIC = "java:global/jms/topic/guest";

    private JMSContext jmsContext;

    private Destination guestEventQueue;

    public AsynchronousGuestEventDispatcher() {
    }

    @Inject
    public AsynchronousGuestEventDispatcher(JMSContext jmsContext) {
        this.jmsContext = jmsContext;
    }

    public void distributeGuestEvent(@Observes @Background Guest guest) {
        LOGGER.info(() -> format("Handling event for %s", guest));
        sendGuestEvent(guest);
    }

    // cannot be applied to constructor
    @Resource(lookup = GUEST_EVENT_TOPIC)
    public void setGuestEventQueue(Destination queue) {
        this.guestEventQueue = queue;
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

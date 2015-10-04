package org.koenighotze.jee7hotel.guest.business.eventing;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSDestinationDefinitions;
import javax.jms.Topic;

/**
 * @author dschmitz
 */
@JMSDestinationDefinitions({
        @JMSDestinationDefinition(
                name = AsynchronousGuestEventDispatcher.GUEST_EVENT_TOPIC,
                resourceAdapter = "jmsra",
                interfaceName = "javax.jms.Topic",
                destinationName = "guestEventTopic")
})
@Stateless // has to be stateless in order to inject jms resources
public class TopicProducer {
    public static final String GUEST_EVENT_TOPIC = "java:global/jms/topic/guest";

    @Resource(name = GUEST_EVENT_TOPIC)
    private Topic guestEvents;

    @Produces
    @GuestEventTopic
    public Topic guestEventTopic() {
        return guestEvents;
    }
}

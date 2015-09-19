package org.koenighotze.jee7hotel.booking.business.resources;

import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSDestinationDefinitions;

/**
 * @author koenighotze
 */

@JMSDestinationDefinitions({
        @JMSDestinationDefinition(
                name = MessagingDefinition.CLASSIC_QUEUE,
                resourceAdapter = "jmsra",
                interfaceName = "javax.jms.Queue",
                destinationName = "classicQueue",
                description = "My Sync Queue"),
        @JMSDestinationDefinition(name = MessagingDefinition.ASYNC_QUEUE,
                resourceAdapter = "jmsra",
                interfaceName = "javax.jms.Queue",
                destinationName = "asyncQueue",
                description = "My Async Queue"),
        @JMSDestinationDefinition(name = MessagingDefinition.SYNC_APP_MANAGED_QUEUE,
                resourceAdapter = "jmsra",
                interfaceName = "javax.jms.Queue",
                destinationName = "syncAppQueue",
                description = "My Sync Queue for App-managed JMSContext"),
        @JMSDestinationDefinition(name = MessagingDefinition.SYNC_CONTAINER_MANAGED_QUEUE,
                resourceAdapter = "jmsra",
                interfaceName = "javax.jms.Queue",
                destinationName = "syncContainerQueue",
                description = "My Sync Queue for Container-managed JMSContext")
})
public class MessagingDefinition {
    public static final String SYNC_APP_MANAGED_QUEUE = "java:global/jms/mySyncAppQueue";
    public static final String SYNC_CONTAINER_MANAGED_QUEUE = "java:global/jms/mySyncContainerQueue";
    public static final String ASYNC_QUEUE = "java:global/jms/myAsyncQueue";
    public static final String CLASSIC_QUEUE = "java:global/jms/classicQueue";
    public static final String RESERVATION_QUEUE = "java:global/jms/reservationQueue";
}

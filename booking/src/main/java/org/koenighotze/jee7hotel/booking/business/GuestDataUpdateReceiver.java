package org.koenighotze.jee7hotel.booking.business;

import org.koenighotze.jee7hotel.booking.domain.GuestModel;
import org.koenighotze.jee7hotel.booking.persistence.GuestModelRepository;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;

/**
 * Receiver for updates to guest data via MQ.
 *
 * @author dschmitz
 */
@MessageDriven(
        activationConfig = {
                @ActivationConfigProperty(propertyName = "destinationLookup",
                        propertyValue = GuestDataUpdateReceiver.GUEST_EVENT_TOPIC),
                @ActivationConfigProperty(propertyName = "destinationType",
                        propertyValue = "javax.jms.Topic")
        }
)
public class GuestDataUpdateReceiver implements MessageListener {
    private static final Logger LOGGER = Logger.getLogger(GuestDataUpdateReceiver.class.getName());
    public static final String GUEST_EVENT_TOPIC = "java:global/jms/topic/guest";

    private GuestModelRepository guestModelRepository;

    public GuestDataUpdateReceiver() {
    }

    @Inject
    public GuestDataUpdateReceiver(GuestModelRepository guestModelRepository) {
        this.guestModelRepository = guestModelRepository;
    }

    @Override
    public void onMessage(Message message) {
        LOGGER.info(() -> "Received message " + message);

        try {
            GuestModel guest = JAXB.unmarshal(new StringReader(message.getBody(String.class)), GuestModel.class);
            LOGGER.info(() -> "Received " + guest);

            guestModelRepository.storeGuestModel(guest);
        } catch (DataBindingException | JMSException e) {
            LOGGER.log(SEVERE, "Cannot handle message " + message, e);
        }
    }
}

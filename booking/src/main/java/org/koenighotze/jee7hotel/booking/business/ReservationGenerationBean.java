package org.koenighotze.jee7hotel.booking.business;

import org.koenighotze.jee7hotel.booking.business.events.BookingMessageTO;
import org.koenighotze.jee7hotel.booking.business.json.BookingMessageTOReader;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.logging.Level.SEVERE;
import static org.koenighotze.jee7hotel.booking.business.resources.MessagingDefinition.RESERVATION_QUEUE;

/**
 * Message driven generator of reservations.
 * <p>
 * Waits for incoming JMS messages and creates appropriate
 * Reservations
 * <p>
 * <p>
 * @author koenighotze
 */
@JMSDestinationDefinition(
        name = RESERVATION_QUEUE,
        resourceAdapter = "jmsra",
        interfaceName = "javax.jms.Queue",
        destinationName = "reservationQueue",
        description = "My Sync Queue")
@MessageDriven(
        activationConfig = {
                @ActivationConfigProperty(propertyName = "destinationLookup",
                        propertyValue = RESERVATION_QUEUE),
                @ActivationConfigProperty(propertyName = "destinationType",
                        propertyValue = "javax.jms.Queue")
        }
)
public class ReservationGenerationBean implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(ReservationGenerationBean.class.getName());

    private BookingService bookingService;

    public ReservationGenerationBean() {
    }

    @Inject
    public ReservationGenerationBean(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Override
    public void onMessage(Message message) {
        LOGGER.info(() -> "Received message " + message);

        LOGGER.info(() -> { // checked exceptions suck in lambdas.
            try {
                return "Payload " + message.getBody(String.class);
            } catch (JMSException e) {
                LOGGER.log(SEVERE, "Cannot handle message " + message, e);
                return "";
            }
        });
        try {
            bookRoom(message);
        } catch (IOException | JMSException e) {
            LOGGER.log(SEVERE, "Cannot process message " + message, e);
        }
    }

    protected BookingMessageTO messageToBookingMessageTO(Message message) throws IOException, JMSException {
        try (ByteArrayInputStream bos = new ByteArrayInputStream(message.getBody(String.class).getBytes(UTF_8))) {
            return new BookingMessageTOReader().readFrom(null, null, null, null, null, bos);
        }
    }

    public void bookRoom(Message message) throws JMSException, IOException {
        BookingMessageTO messageTO = messageToBookingMessageTO(message);

        LOGGER.info(() -> "Booking room " + messageTO.getRoom() + " for guest " + messageTO.getGuest());
        this.bookingService.bookRoom("" + messageTO.getGuest(), messageTO.getRoom(), messageTO.getCheckin(), messageTO.getCheckout());
        LOGGER.info(() -> "Booked room " + messageTO.getRoom() + " for guest " + messageTO.getGuest());
    }
}

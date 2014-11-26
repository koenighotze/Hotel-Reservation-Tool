package org.koenighotze.jee7hotel.business;

import org.koenighotze.jee7hotel.business.events.BookingMessageTO;
import org.koenighotze.jee7hotel.business.json.BookingMessageTOReader;
import org.koenighotze.jee7hotel.domain.Guest;
import org.koenighotze.jee7hotel.domain.Reservation;
import org.koenighotze.jee7hotel.domain.Room;
import org.koenighotze.jee7hotel.frontend.model.Booking;
import org.koenighotze.jee7hotel.resources.MessagingDefinition;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.*;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Message driven generator of reservations.
 * <p>
 * Waits for incoming JMS messages and creates appropriate
 * Reservations
 * <p>
 * <p>
 * Created by dschmitz on 20.11.14.
 */
//@JMSDestinationDefinition(
//        name = "java:app/jee7hotel/reservationQueue",
//        interfaceName = "javax.jms.Queue",
//        destinationName = "reservationQueue"
//)
/* Activate the traffic-rar resource adapter from this MDB */
//@MessageDriven(
//        activationConfig = {
//            @ActivationConfigProperty(
//                    propertyName = "port",
//                    propertyValue = "4008"
//            )
//        }
//)
// TODO activationSpec
// TODO Trigger Event

//@JMSDestinationDefinition(name = "java:app/jee7hotel/reservationQueue", interfaceName = "javax.jms.Queue")
@MessageDriven(
        activationConfig = {
                @ActivationConfigProperty(propertyName = "destinationLookup",
                        propertyValue = MessagingDefinition.CLASSIC_QUEUE),
                @ActivationConfigProperty(propertyName = "destinationType",
                        propertyValue = "javax.jms.Queue")
        }
)
public class ReservationGenerationBean implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(ReservationGenerationBean.class.getName());
    @Inject
    private RoomService roomService;
    @Inject
    private BookingService bookingService;
    @Inject
    private GuestService guestService;

    public ReservationGenerationBean() {
    }

    public ReservationGenerationBean(RoomService roomService, GuestService guestService, BookingService bookingService) {
        this.bookingService = bookingService;
        this.guestService = guestService;
        this.roomService = roomService;
    }

    @Override
    public void onMessage(Message message) {
        LOGGER.info(() -> "Received message " + message);

        LOGGER.info(() -> { // checked exceptions suck in lambdas.
            try {
                return "Payload " + message.getBody(String.class);
            } catch (JMSException e) {
                LOGGER.log(Level.SEVERE, "Cannot handle message " + message, e);
                return "";
            }
        });
        try {
            bookRoom(message);
        } catch (IOException | JMSException e) {
            LOGGER.log(Level.SEVERE, "Cannot process message " + message, e);
        }


    }

    protected BookingMessageTO messageToBookingMessageTO(Message message) throws IOException, JMSException {
        try (ByteArrayInputStream bos = new ByteArrayInputStream(message.getBody(String.class).getBytes(StandardCharsets.UTF_8))) {
            return new BookingMessageTOReader().readFrom(null, null, null, null, null, bos);
        }
    }

    public void bookRoom(Message message) throws JMSException, IOException {
        BookingMessageTO messageTO = messageToBookingMessageTO(message);

        Optional<Guest> guest = this.guestService.findById(messageTO.getGuest());
        if (!guest.isPresent()) {
            LOGGER.warning("No such guest " + messageTO.getGuest());
            return;
        }

        Optional<Room> room = this.roomService.findRoomByNumber(messageTO.getRoom());
        if (!room.isPresent()) {
            LOGGER.warning("No such room " + messageTO.getRoom());
            return;
        }

        LOGGER.info(() -> "Booking room " + room + " for guest " + guest);
        Reservation reservation = this.bookingService.bookRoom(guest.get(), room.get(), messageTO.getCheckin(), messageTO.getCheckout());
        LOGGER.info(() -> "Booked room " + room + " for guest " + guest);
    }
}

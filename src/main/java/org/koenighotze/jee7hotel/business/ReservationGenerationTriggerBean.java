package org.koenighotze.jee7hotel.business;

import org.koenighotze.jee7hotel.business.events.BookingMessageTO;
import org.koenighotze.jee7hotel.business.json.BookingMessageTOWriter;
import org.koenighotze.jee7hotel.domain.Guest;
import org.koenighotze.jee7hotel.domain.Room;
import org.koenighotze.jee7hotel.resources.MessagingDefinition;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.logging.Logger;

/**
 * Sample bean for sending messages to a MDB.
 *
 *
 * Created by dschmitz on 25.11.14.
 */
@Named
@Stateless
@Path("bookings")
public class ReservationGenerationTriggerBean {

    private static final Logger LOGGER = Logger.getLogger(ReservationGenerationBean.class.getName());

    @Inject
    private JMSContext jmsContext;

    @Resource(lookup = MessagingDefinition.CLASSIC_QUEUE)
    private Queue queue;


    private String bookingMessageTOToJson(BookingMessageTO messageTO) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            new BookingMessageTOWriter().writeTo(messageTO, null, null, null, MediaType.APPLICATION_JSON_TYPE, null, bos);
            bos.flush();
            return bos.toString(StandardCharsets.UTF_8.name());
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response triggerReservation(BookingMessageTO messageTO) {
        try {
//            BookingMessageTO messageTO = new BookingMessageTO(guest.getId(), room.getRoomNumber(), checkin, checkout);

            LOGGER.fine(() -> "Sending " + messageTO + " to " + this.queue);

            TextMessage textMessage = this.jmsContext.createTextMessage(bookingMessageTOToJson(messageTO));
            this.jmsContext.createProducer().send(this.queue, textMessage);

            URI uri = URI.create(textMessage.getJMSMessageID());
            return Response.created(uri).build();
        } catch (IOException | JMSException e) {
            throw new RuntimeException("Cannot handle reservation", e);
        }
    }
}

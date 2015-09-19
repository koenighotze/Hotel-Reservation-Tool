package org.koenighotze.jee7hotel.booking.business;

import org.koenighotze.jee7hotel.booking.business.events.BookingMessageTO;
import org.koenighotze.jee7hotel.booking.business.json.BookingMessageTOWriter;

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
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.koenighotze.jee7hotel.booking.business.resources.MessagingDefinition.RESERVATION_QUEUE;

/**
 * Sample bean for sending messages to a MDB.
 *
 * @author koenighotze
 */
@Named
@Stateless
@Path("trigger")
public class ReservationGenerationTriggerBean {

    private static final Logger LOGGER = Logger.getLogger(ReservationGenerationBean.class.getName());

    private JMSContext jmsContext;

    private Queue queue;

    public ReservationGenerationTriggerBean() {
    }

    @Inject
    public ReservationGenerationTriggerBean(JMSContext jmsContext) {
        this.jmsContext = jmsContext;
    }

    // cannot be applied to constructor
    @Resource(lookup = RESERVATION_QUEUE)
    public void setReservationQueue(Queue queue) {
        this.queue = queue;
    }

    private String bookingMessageTOToJson(BookingMessageTO messageTO) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            new BookingMessageTOWriter().writeTo(messageTO, null, null, null, APPLICATION_JSON_TYPE, null, bos);
            bos.flush();
            return bos.toString(UTF_8.name());
        }
    }

    @POST
    @Consumes(APPLICATION_JSON)
    public Response triggerReservation(BookingMessageTO messageTO) {
        try {
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

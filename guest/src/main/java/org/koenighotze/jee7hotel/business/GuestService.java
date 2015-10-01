package org.koenighotze.jee7hotel.business;

import org.koenighotze.jee7hotel.business.eventsource.EventSourceInterceptor;
import org.koenighotze.jee7hotel.business.logging.PerformanceLogger;
import org.koenighotze.jee7hotel.domain.Guest;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;
import javax.jms.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.ws.rs.*;
import javax.xml.bind.JAXB;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static java.lang.String.format;
import static java.util.Optional.*;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static org.koenighotze.jee7hotel.domain.Guest.GUEST_FIND_BY_PUBLIC_ID;

/**
 * Sample for a REST-based bean, that can also be used locally.
 *
 * @author dschmitz
 */
@Named
@Stateless
@Path("guest")
@Interceptors({
        PerformanceLogger.class,
        EventSourceInterceptor.class
})
@JMSDestinationDefinitions({
        @JMSDestinationDefinition(
                name = GuestService.GUEST_EVENT_TOPIC,
                resourceAdapter = "jmsra",
                interfaceName = "javax.jms.Topic",
                destinationName = "guestEventTopic")
})
public class GuestService {
    private static final Logger LOGGER = Logger.getLogger(GuestService.class.getName());

    public static final String GUEST_EVENT_TOPIC = "java:global/jms/topic/guest";

    @PersistenceContext
    private EntityManager em;

    private JMSContext jmsContext;

    private Destination guestEventQueue;

    public GuestService() {
    }

    @Inject
    public GuestService(JMSContext jmsContext) {
        this.jmsContext = jmsContext;
    }

    // cannot be applied to constructor
    @Resource(lookup = GUEST_EVENT_TOPIC)
    public void setGuestEventQueue(Destination queue) {
        this.guestEventQueue = queue;
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @GET
    @Consumes({APPLICATION_XML, APPLICATION_JSON})
    public void saveGuest(Guest guest) {
        LOGGER.info(() -> "Saving guest " + guest);
        this.em.persist(guest);
        sendGuestEvent(guest);
    }

    // TODO: Extract to CDI Events
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
        }
        catch (JMSRuntimeException e) {
            LOGGER.log(SEVERE, e, () -> "Cannot send message due to technical reasons!");
        }
    }

    @GET
    @Produces({APPLICATION_XML, APPLICATION_JSON})
    @Path("/{guestId}")
    public Guest findSingleGuestById(@PathParam("guestId") Long guestId) {
        return this.em.find(Guest.class, guestId);
    }

    @GET
    @Produces({APPLICATION_XML, APPLICATION_JSON})
    public List<Guest> getAllGuests() {
        CriteriaQuery<Guest> cq = this.em.getCriteriaBuilder().createQuery(Guest.class);
        cq.select(cq.from(Guest.class));
        return this.em.createQuery(cq).getResultList();
    }

    public Optional<Guest> findById(@PathParam("guestId") Long guestId) {
        Guest guest = this.em.find(Guest.class, guestId);
        return ofNullable(guest);
    }

    public Optional<Guest> updateGuestDetails(Guest guest) {
        if (null == guest || null == guest.getId()) {
            return empty();
        }
        return of(this.em.merge(guest));
    }

    public Optional<Guest> findByPublicId(String publicId) {
        TypedQuery<Guest> query = em.createNamedQuery(GUEST_FIND_BY_PUBLIC_ID, Guest.class);
        query.setParameter("publicId", publicId);
        List<Guest> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return empty();
        }
        return of(resultList.get(0));
    }
}

package org.koenighotze.jee7hotel.business;

import org.koenighotze.jee7hotel.business.eventing.Background;
import org.koenighotze.jee7hotel.domain.Guest;
import org.koenighotze.jee7hotel.framework.application.logging.PerformanceLogger;
import org.koenighotze.jee7hotel.framework.eventing.eventsource.EventSourceInterceptor;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.ws.rs.*;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static java.util.Optional.*;
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
public class GuestService {
    private static final Logger LOGGER = Logger.getLogger(GuestService.class.getName());

    @PersistenceContext
    private EntityManager em;

    private Event<Guest> guestEvents;

    public GuestService() {
    }

    @Inject
    public GuestService(@Background Event<Guest> guestEvents) {
        this.guestEvents = guestEvents;
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @GET
    @Consumes({APPLICATION_XML, APPLICATION_JSON})
    public void saveGuest(Guest guest) {
        LOGGER.info(() -> "Saving guest " + guest);
        this.em.persist(guest);

        guestEvents.fire(guest);
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

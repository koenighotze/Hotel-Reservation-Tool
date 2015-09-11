package org.koenighotze.jee7hotel.business;

import org.koenighotze.jee7hotel.business.eventsource.EventSourceInterceptor;
import org.koenighotze.jee7hotel.business.logging.PerformanceLogger;
import org.koenighotze.jee7hotel.domain.Guest;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static java.util.Optional.*;
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

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    public void saveGuest(Guest guest) {
        LOGGER.info(() -> "Saving guest " + guest);
        this.em.persist(guest);
    }

    @GET
    @Produces({"application/xml", "application/json"})
    @Path("/{guestId}")
    public Guest findSingleGuestById(@PathParam("guestId") Long guestId) {
        return this.em.find(Guest.class, guestId);
    }

    @GET
    @Produces({"application/xml", "application/json"})
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

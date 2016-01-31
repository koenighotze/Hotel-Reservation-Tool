package org.koenighotze.jee7hotel.guest.business;

import org.koenighotze.jee7hotel.framework.application.logging.PerformanceLogger;
import org.koenighotze.jee7hotel.framework.eventing.eventsource.EventSourceInterceptor;
import org.koenighotze.jee7hotel.guest.business.eventing.Background;
import org.koenighotze.jee7hotel.guest.domain.Guest;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.validation.constraints.NotNull;
import javax.ws.rs.PathParam;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static java.lang.Boolean.TRUE;
import static java.util.Optional.*;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Sample for a REST-based bean, that can also be used locally.
 *
 * @author dschmitz
 */
@Named
@Stateless
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

    public void saveGuest(Guest guest) {
        LOGGER.info(() -> "Saving guest " + guest);

        if (isBlank(guest.getPublicId())) {
            guest.setPublicId(randomUUID().toString());
        }

        this.em.persist(guest);

        guestEvents.fire(guest);
    }

    public Guest findSingleGuestById(@PathParam("guestId") Long guestId) {
        return this.em.find(Guest.class, guestId);
    }

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
        TypedQuery<Guest> query = em.createNamedQuery(Guest.GUEST_FIND_BY_PUBLIC_ID, Guest.class);
        query.setParameter("publicId", publicId);
        List<Guest> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return empty();
        }
        return of(resultList.get(0));
    }

    public boolean deleteGuest(@NotNull String publicId) {
        return findByPublicId(publicId).flatMap( g -> {
          em.remove(g);
          return of(TRUE);
        }).orElse(false);
    }
}

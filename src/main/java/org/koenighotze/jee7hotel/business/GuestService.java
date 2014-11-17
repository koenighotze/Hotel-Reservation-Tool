package org.koenighotze.jee7hotel.business;

import org.koenighotze.jee7hotel.domain.Guest;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Sample for a REST-based bean, that can also be used locally.
 *
 * @author dschmitz
 */
@Named
@Stateless
@Path("guest")
// @RolesAllowed({ "ADMIN" })
//@PerformanceLog
public class GuestService {
    private static final Logger LOGGER = Logger.getLogger(GuestService.class.getName());

    @PersistenceContext
    private EntityManager em;

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    public void saveGuest(Guest guest) {
        this.em.persist(guest);
    }


    // TODO: protect using basic auth!
    @GET
    @Produces({"application/xml", "application/json"})
    public List<Guest> getAllGuests() {
        CriteriaQuery cq = this.em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Guest.class));
        return this.em.createQuery(cq).getResultList();
    }

    public Optional<Guest> findById(Long guestId) {
        Guest guest = this.em.find(Guest.class, guestId);

        return Optional.ofNullable(guest);
    }

    public Optional<Guest> updateGuestDetails(Guest guest) {
        if (null == guest || null == guest.getId()) {
            return Optional.empty();
        }
        return Optional.of(this.em.merge(guest));
    }
}

package org.koenighotze.jee7hotel.business;

import org.koenighotze.jee7hotel.domain.Guest;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Sample for a REST-based bean, that can also be used locally.
 * 
 * @author dschmitz
 */
@Named
@ApplicationScoped
@Path("guest")
@Transactional
// @RolesAllowed({ "ADMIN" })
public class GuestService {
    private static final Logger LOGGER = Logger.getLogger(GuestService.class.getName());
    
    @PersistenceContext
    private EntityManager em;

    
    void setEntityManager(EntityManager em) {
        this.em = em;
    }
    
    public void saveGuest(Guest guest) {
        this.em.persist(guest);
    }
       
    
    // TODO: protect using basic auth!
    @GET
    @Produces({"application/xml", "application/json"})
    public List<Guest> findAllGuests() {
        CriteriaQuery cq = this.em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Guest.class));
        return this.em.createQuery(cq).getResultList();        
    }

    public Guest findById(Long guestId) {
        return this.em.find(Guest.class, guestId);
    }

    public Guest updateGuestDetails(Guest guest) {
        if (null == guest || null == guest.getId()) {
            return null;
        }        
        return this.em.merge(guest);
    }
}
package demo.business;

import demo.domain.Guest;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
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
 *
 * @author dschmitz
 */
@Named
@ApplicationScoped
@Path("guest")
// @RolesAllowed({ "ADMIN" })
public class GuestService {
    @PersistenceContext
    private EntityManager em;

    
    void setEntityManager(EntityManager em) {
        this.em = em;
    }
    
    public void saveGuest(Guest guest) {
        this.em.persist(guest);
    }
       
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

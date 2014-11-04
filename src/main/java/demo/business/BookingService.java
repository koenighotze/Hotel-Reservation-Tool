

package demo.business;

import demo.domain.Guest;
import demo.domain.Reservation;
import demo.domain.Reservation_;
import demo.domain.Room;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

/**
 *
 * @author dschmitz
 */
@Named
@ApplicationScoped
@Transactional
public class BookingService {
    @PersistenceContext
    private EntityManager em;
    
    public void cancelReservation(Reservation reservation) {
    }
    
    
    public String newReservationNumer() {
        return UUID.randomUUID().toString();
    }
    
    public List<Reservation> findReservationForGuest(Guest guest) {        
        TypedQuery<Reservation> query = this.em.createQuery("from Reservation r where r.guest = :guest", Reservation.class);
        query.setParameter("guest", guest);
        return query.getResultList();
        // TODO: add generation of metaclasses
//        CriteriaQuery cq = this.em.getCriteriaBuilder().createQuery();
//        
//        Metamodel metamodel = em.getMetamodel();
//        
//        EntityType<Reservation> entity = metamodel.entity(Reservation.class);
//        
//        cq.select(cq.from(Reservation.class)).where(entity.get(Reservation_)
//        return this.em.createQuery(cq).getResultList(); 
    }
    
    public List<Reservation> getAllReservations() {
        CriteriaQuery cq = this.em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Reservation.class));
        return this.em.createQuery(cq).getResultList(); 
    }
    
    
    public Reservation bookRoom(Guest guest, Room room, Date checkin, Date checkout) {
        Reservation reservation = new Reservation(guest, newReservationNumer(), room, checkin, checkout);
                
        this.em.persist(reservation);
        
        return reservation;
    }

    void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }
}

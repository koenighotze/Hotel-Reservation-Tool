

package demo.business;

import demo.domain.Guest;
import demo.domain.Room;
import demo.domain.RoomEquipment;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;

/**
 *
 * @author dschmitz
 * 
 */
@Named
@ApplicationScoped
@Transactional
public class RoomService {
    @PersistenceContext
    private EntityManager em;
    
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
    
    public List<Room> findAvailableRooms(Date checkinDate, Date checkoutDate, RoomEquipment equiment) {
        return null;
    }
    
    public List<Room> getAllRooms() {
        CriteriaQuery cq = this.em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Room.class));
        return this.em.createQuery(cq).getResultList(); 
    }

    public Room findRoomByNumber(String roomNumber) {
        TypedQuery<Room> query = this.em.createQuery("from Room r where r.roomNumber = :number", Room.class);
        query.setParameter("number", roomNumber);
        List<Room> resultList = query.getResultList();
        if (resultList.size() == 0) {
            return null;
        }
        return resultList.get(0);
    }

    public Room findRoomById(Long roomId) {
        return this.em.find(Room.class, roomId);
    }
    
}
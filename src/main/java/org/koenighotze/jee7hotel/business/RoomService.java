

package org.koenighotze.jee7hotel.business;

import org.koenighotze.jee7hotel.domain.Guest;
import org.koenighotze.jee7hotel.domain.Room;
import org.koenighotze.jee7hotel.domain.RoomEquipment;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    private static final Logger LOGGER = Logger.getLogger(RoomService.class.getName());
    @PersistenceContext
    private EntityManager em;
    
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
    
    public List<Room> findAvailableRooms(Date checkinDate, Date checkoutDate, RoomEquipment equiment) {
        throw new UnsupportedOperationException("not implemented yet");
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
            LOGGER.log(Level.WARNING, "Cannot find room {0}", roomNumber);
            return null;
        }
        return resultList.get(0);
    }

    public Room findRoomById(Long roomId) {
        return this.em.find(Room.class, roomId);
    }
    
}

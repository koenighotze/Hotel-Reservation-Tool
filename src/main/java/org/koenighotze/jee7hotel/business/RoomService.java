

package org.koenighotze.jee7hotel.business;

import org.koenighotze.jee7hotel.business.eventsource.EventSourceInterceptor;
import org.koenighotze.jee7hotel.business.logging.PerformanceLog;
import org.koenighotze.jee7hotel.business.logging.PerformanceLogger;
import org.koenighotze.jee7hotel.domain.Room;
import org.koenighotze.jee7hotel.domain.RoomEquipment;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dschmitz
 *
 */
@Named
@Stateless
@Interceptors({
        PerformanceLogger.class,
        EventSourceInterceptor.class
})
public class RoomService {

    private static final Logger LOGGER = Logger.getLogger(RoomService.class.getName());
    @PersistenceContext
    private EntityManager em;

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    public List<Room> findAvailableRooms(Date checkinDate, Date checkoutDate, RoomEquipment equiment) {
        TypedQuery<Room> query = this.em.createNamedQuery("Room.findAvailable", Room.class);
        return query.getResultList();
    }

    public List<Room> getAllRooms() {
        CriteriaQuery<Room> cq = this.em.getCriteriaBuilder().createQuery(Room.class);
        cq.select(cq.from(Room.class));
        return this.em.createQuery(cq).getResultList();
    }

    public Optional<Room> findRoomByNumber(String roomNumber) {
        TypedQuery<Room> query = this.em.createNamedQuery("Room.findByRoomNumber", Room.class);
        query.setParameter("number", roomNumber);
        List<Room> resultList = query.getResultList();
        if (resultList.size() == 0) {
            LOGGER.log(Level.WARNING, "Cannot find room {0}", roomNumber);
            return Optional.empty();
        }
        return Optional.of(resultList.get(0));
    }

    public Optional<Room> findRoomById(Long roomId) {
        return Optional.ofNullable(this.em.find(Room.class, roomId));
    }

}

package demo.business;

import demo.domain.Guest;
import demo.domain.Reservation;
import demo.domain.ReservationStatus;
import demo.domain.Room;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 *
 * @author dschmitz
 */
@Named
@ApplicationScoped
@Transactional
@Path("bookings")
public class BookingService {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Event<NewReservationEvent> reservationEvents;
    
    @Inject
    private Event<ReservationStatusChangeEvent> reservationConfirmedEvents;
    
    private static final Logger LOGGER = Logger.getLogger(BookingService.class.getName());
    
    public void cancelReservation(Reservation reservation) {
        // TODO: refactor such that op. of. lock is used
        Reservation current = this.em.find(Reservation.class, reservation.getId());
        if (null == current) {
            LOGGER.log(Level.WARNING, "Cannot find reservation {0}", reservation.getId());
            return;
        }
        
        current.setReservationStatus(ReservationStatus.CANCELED);
    }

    // TODO: extract to calculation strategy or similar
    public BigDecimal calculateRateFor(Room room, Date checkin, Date checkout) {
        // rather simple...
        DateTime dtCi = new DateTime(checkin);
        DateTime dtCo = new DateTime(checkout);
        int days = Days.daysBetween(dtCi, dtCo).getDays();

        BigDecimal rate = BigDecimal.ONE;

        switch (room.getRoomEquipment()) {
            case BUDGET:
                rate = BigDecimal.valueOf(60L);
                break;
            case STANDARD:
                rate = BigDecimal.valueOf(92L);
                break;
            case SUPERIOR:
                rate = BigDecimal.valueOf(210L);
                break;
            default:
                throw new IllegalArgumentException("Unknown equipment " + room);
        }

        return rate.multiply(BigDecimal.valueOf(days));
    }

    // 
    public String newReservationNumer() {
        return UUID.randomUUID().toString();
    }

    public List<Reservation> findReservationForGuest(Guest guest) {
        TypedQuery<Reservation> query = this.em.createQuery("from Reservation r where r.guest = :guest", Reservation.class);
        query.setParameter("guest", guest);
        return query.getResultList();
    }

    
    // TODO: protect using basic auth
    @GET
    @Produces({"application/xml", "application/json"})
    public List<Reservation> getAllReservations() {
        CriteriaQuery cq = this.em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Reservation.class));
        return this.em.createQuery(cq).getResultList();
    }

    public Reservation bookRoom(Guest guest, Room room, Date checkin, Date checkout) {
        Reservation reservation = new Reservation(guest, newReservationNumer(), room, checkin, checkout, calculateRateFor(room, checkin, checkout));

        this.em.persist(reservation);
        
        if (null != this.reservationEvents)
            this.reservationEvents.fire(new NewReservationEvent(reservation.getReservationNumber()));
        
        return reservation;
    }

    void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }

    public Reservation findReservationByNumber(String reservationNumber) {
        TypedQuery<Reservation> query = this.em.createQuery("from Reservation r where r.reservationNumber = :number", Reservation.class);
        query.setParameter("number", reservationNumber);
        if (query.getResultList().size() == 0) {
            LOGGER.log(Level.WARNING, "Cannot find reservation {0}", reservationNumber);
            return null;
        }

        return query.getResultList().get(0);
    }

    public void reopenReservation(Reservation reservation) {
        // TODO: refactor such that op. of. lock is used
        Reservation current = this.em.find(Reservation.class, reservation.getId());
        current.setReservationStatus(ReservationStatus.OPEN);
    }

    public void confirmReservation(String reservationNumber) {
        Reservation reservation = findReservationByNumber(reservationNumber);
        
        if (null == reservation) {
            LOGGER.log(Level.WARNING, "Cannot find reservation {0}", reservationNumber);
            return;
        } 
        reservation.setReservationStatus(ReservationStatus.CONFIRMED);
        
        this.reservationConfirmedEvents.fire(new ReservationStatusChangeEvent(reservationNumber, null, ReservationStatus.CONFIRMED));
    }
}

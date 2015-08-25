package org.koenighotze.jee7hotel.booking.business;

import org.koenighotze.jee7hotel.booking.business.events.NewReservationEvent;
import org.koenighotze.jee7hotel.booking.business.events.ReservationStatusChangeEvent;
import org.koenighotze.jee7hotel.booking.domain.Reservation;
import org.koenighotze.jee7hotel.booking.domain.ReservationStatus;
import org.koenighotze.jee7hotel.booking.domain.RoomEquipment;
import org.koenighotze.jee7hotel.business.eventsource.EventSource;
import org.koenighotze.jee7hotel.business.logging.PerformanceLog;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dschmitz
 */
@Named
@Stateless
@Path("bookings")
// declare interceptor w/o using a stereotype (annotation based) binding
// this way the interceptor will work w/o beans.xml
//@Interceptors({
//        PerformanceLogger.class
//        //,
//        //EventSourceInterceptor.class
//})
@EventSource
@PerformanceLog
public class BookingService {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Event<NewReservationEvent> reservationEvents;

    @Inject
    private Event<ReservationStatusChangeEvent> reservationStateChangeEvents;
    
    private static final Logger LOGGER = Logger.getLogger(BookingService.class.getName());

    public void setReservationStateChangeEvents(Event<ReservationStatusChangeEvent> reservationStateChangeEvents) {
        this.reservationStateChangeEvents = reservationStateChangeEvents;
    }

    public void setReservationEvents(Event<NewReservationEvent> reservationEvents) {

        this.reservationEvents = reservationEvents;
    }

    // TODO refactor to reservation number
    public void cancelReservation(String reservationNumber) {
        Optional<Reservation> reservation = findReservationByNumber(reservationNumber);
        reservation.ifPresent(r -> {
            r.setReservationStatus(ReservationStatus.CANCELED);
            this.reservationStateChangeEvents.fire(new ReservationStatusChangeEvent(r.getReservationNumber(), null, ReservationStatus.CONFIRMED));
        });
    }

    // TODO: extract to calculation strategy or similar
    public BigDecimal calculateRateFor(RoomEquipment roomEquipment, LocalDate checkin, LocalDate checkout) {
        // rather simple...
        long days = checkin.until(checkout, ChronoUnit.DAYS);

        BigDecimal rate = null;

        switch (roomEquipment) {
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
                throw new IllegalArgumentException("Unknown equipment " + roomEquipment);
        }

        return rate.multiply(BigDecimal.valueOf(days));
    }

    // TODO: extract to real number producer bean
    public String newReservationNumber() {
        return UUID.randomUUID().toString();
    }

    public List<Reservation> findReservationForGuest(String guestId) {
        TypedQuery<Reservation> query = this.em.createNamedQuery("Reservation.findByGuest", Reservation.class);
        query.setParameter("guest", guestId);
        return query.getResultList();
    }

    
    // TODO: protect using basic auth
    @GET
    @Produces({"application/xml", "application/json"})
    public List<Reservation> getAllReservations() {
        CriteriaQuery<Reservation> cq = this.em.getCriteriaBuilder().createQuery(Reservation.class);
        cq.select(cq.from(Reservation.class));
        return new ArrayList(this.em.createQuery(cq).getResultList());
    }

    @GET
    @Produces({"application/xml", "application/json"})
    @Path("{id}")
    public Reservation getReservation(@PathParam("id") Long id) {
        return this.em.find(Reservation.class, id);
    }


    public Reservation bookRoom(String guestId, String roomId, LocalDate checkin, LocalDate checkout) {
        // TODO: Temp fix
        Reservation reservation = 
                new Reservation(guestId, newReservationNumber(), roomId,
                        checkin, checkout, calculateRateFor(RoomEquipment.BUDGET, checkin, checkout));

        LOGGER.info(() -> "Storing " + reservation);
        this.em.persist(reservation);

        this.reservationEvents.fire(new NewReservationEvent(reservation.getReservationNumber()));

        this.em.flush();
        return reservation;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }

    public Optional<Reservation> findReservationByNumber(String reservationNumber) {
        TypedQuery<Reservation> query = this.em.createNamedQuery("Reservation.findByReservationNumber", Reservation.class);
        query.setParameter("number", reservationNumber);
        if (query.getResultList().size() == 0) {
            LOGGER.log(Level.WARNING, "Cannot find reservation {0}", reservationNumber);
            return Optional.empty();
        }

        return Optional.of(query.getResultList().get(0));
    }

    public void reopenReservation(String reservationNumber) {
        Optional<Reservation> reservation = findReservationByNumber(reservationNumber);
        reservation.ifPresent(r -> {
            r.setReservationStatus(ReservationStatus.OPEN);
            this.reservationStateChangeEvents.fire(new ReservationStatusChangeEvent(reservationNumber, null, ReservationStatus.OPEN));
        });
    }

    public void confirmReservation(String reservationNumber) {
        Optional<Reservation> reservation = findReservationByNumber(reservationNumber);
        
        reservation.ifPresent(r -> {
            r.setReservationStatus(ReservationStatus.CONFIRMED);
            this.reservationStateChangeEvents.fire(new ReservationStatusChangeEvent(reservationNumber, null, ReservationStatus.CONFIRMED));
        } );
        

        if (!reservation.isPresent()) {
            LOGGER.log(Level.WARNING, "Cannot find reservation {0}", reservationNumber);
        }
    }
}

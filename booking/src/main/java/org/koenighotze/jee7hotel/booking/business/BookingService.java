package org.koenighotze.jee7hotel.booking.business;

import org.koenighotze.jee7hotel.booking.business.events.NewReservationEvent;
import org.koenighotze.jee7hotel.booking.business.events.ReservationStatusChangeEvent;
import org.koenighotze.jee7hotel.booking.domain.Reservation;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;
import static java.util.UUID.randomUUID;
import static java.util.logging.Level.WARNING;
import static org.koenighotze.jee7hotel.booking.domain.ReservationStatus.*;
import static org.koenighotze.jee7hotel.booking.domain.RoomEquipment.BUDGET;

/**
 * @author dschmitz
 */
@Named
@Stateless
// declare interceptor w/o using a stereotype (annotation based) binding
// this way the interceptor will work w/o beans.xml
//@Interceptors({
//        PerformanceLogger.class,
//        EventSourceInterceptor.class
//})
//@EventSource
//@PerformanceLog
public class BookingService {
    private static final Logger LOGGER = Logger.getLogger(BookingService.class.getName());

    private ReservationCostCalculator reservationCostCalculator;

    private EntityManager em;

    private Event<NewReservationEvent> reservationEvents;

    private Event<ReservationStatusChangeEvent> reservationStateChangeEvents;

    public BookingService() {
    }

    @Inject
    public BookingService(Event<NewReservationEvent> reservationEvents, Event<ReservationStatusChangeEvent> reservationStateChangeEvents, ReservationCostCalculator reservationCostCalculator) {
        this.reservationEvents = requireNonNull(reservationEvents);
        this.reservationStateChangeEvents = requireNonNull(reservationStateChangeEvents);
        this.reservationCostCalculator = requireNonNull(reservationCostCalculator);
    }

    public void setReservationStateChangeEvents(Event<ReservationStatusChangeEvent> reservationStateChangeEvents) {
        this.reservationStateChangeEvents = reservationStateChangeEvents;
    }

    public void setReservationEvents(Event<NewReservationEvent> reservationEvents) {
        this.reservationEvents = reservationEvents;
    }

    public boolean cancelReservation(String reservationNumber) {
        LOGGER.info(() -> "Cancelling " + reservationNumber);
        Optional<Reservation> reservation = findReservationByNumber(reservationNumber);

        reservation.ifPresent(r -> {
            r.setReservationStatus(CANCELED);
            this.reservationStateChangeEvents.fire(new ReservationStatusChangeEvent(r.getReservationNumber(), null, CONFIRMED));
        });

        return false;
    }

    public String newReservationNumber() {
        return randomUUID().toString();
    }

    public List<Reservation> findReservationForGuest(String guestId) {
        TypedQuery<Reservation> query = this.em.createNamedQuery("Reservation.findByGuest", Reservation.class);
        query.setParameter("guest", guestId);
        return query.getResultList();
    }

    public List<Reservation> getAllReservations() {
        CriteriaQuery<Reservation> cq = this.em.getCriteriaBuilder().createQuery(Reservation.class);
        cq.select(cq.from(Reservation.class));
        return new ArrayList<>(this.em.createQuery(cq).getResultList());
    }

    public Reservation getReservation(Long id) {
        return this.em.find(Reservation.class, id);
    }

    public Reservation bookRoom(String guestId, String roomId, LocalDate checkin, LocalDate checkout) {
        Reservation reservation =
                new Reservation(guestId, newReservationNumber(), roomId,
                        checkin, checkout, reservationCostCalculator.calculateRateFor(BUDGET, checkin, checkout));

        LOGGER.info(() -> "Storing " + reservation);
        this.em.persist(reservation);

        this.reservationEvents.fire(new NewReservationEvent(reservation.getReservationNumber()));

        this.em.flush();
        return reservation;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }

    public Optional<Reservation> findReservationByNumber(String reservationNumber) {
        TypedQuery<Reservation> query = this.em.createNamedQuery("Reservation.findByReservationNumber", Reservation.class);
        query.setParameter("number", reservationNumber);
        if (query.getResultList().size() == 0) {
            LOGGER.log(WARNING, "Cannot find reservation {0}", reservationNumber);
            return Optional.empty();
        }

        return Optional.of(query.getResultList().get(0));
    }

    public void reopenReservation(String reservationNumber) {
        LOGGER.info(() -> "Reopening " + reservationNumber);
        Optional<Reservation> reservation = findReservationByNumber(reservationNumber);
        reservation.ifPresent(r -> {
            r.setReservationStatus(OPEN);
            this.reservationStateChangeEvents.fire(new ReservationStatusChangeEvent(reservationNumber, null, OPEN));
        });
    }

    public void confirmReservation(String reservationNumber) {
        LOGGER.info(() -> "Confirming " + reservationNumber);

        Optional<Reservation> reservation = findReservationByNumber(reservationNumber);

        reservation.ifPresent(r -> {
            r.setReservationStatus(CONFIRMED);
            this.reservationStateChangeEvents.fire(new ReservationStatusChangeEvent(reservationNumber, null, CONFIRMED));
        });

        if (!reservation.isPresent()) {
            LOGGER.log(WARNING, "Cannot find reservation {0}", reservationNumber);
        }
    }
}

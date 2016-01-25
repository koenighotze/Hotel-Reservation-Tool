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
import javax.ws.rs.*;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;
import static java.util.logging.Level.WARNING;
import static org.koenighotze.jee7hotel.booking.domain.ReservationStatus.*;
import static org.koenighotze.jee7hotel.booking.domain.RoomEquipment.BUDGET;

/**
 * @author dschmitz
 */
@Named
@Stateless
@Path("bookings")
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

    @Context
    private ResourceContext resourceContext;

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
        return UUID.randomUUID().toString();
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

    @GET
    @Produces({"application/xml", "application/json"})
    public Response allReservations() {
        List<Reservation> allReservations = getAllReservations();
        return Response.ok(allReservations).build();
    }

    @GET
    @Produces({"application/xml", "application/json"})
    @Path("/{publicId}")
    public Response reservation(@PathParam("publicId") String id) {
        Optional<Reservation> reservation = findReservationByNumber(id);

        if (reservation.isPresent()) {

            LOGGER.info(() -> "Returning reservation " + reservation.get());
            return Response.ok(reservation.get()).build();
        }

        return Response.ok().status(Response.Status.NOT_FOUND).build();
    }

    public Reservation getReservation(Long id) {
        return this.em.find(Reservation.class, id);
    }


    @POST
    @Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON })
    public Response reservation(Reservation booking,  @Context UriInfo uriInfo) {
        Reservation reservation = bookRoom(booking.getGuest(), booking.getAssignedRoom(), booking.getCheckinDate(), booking.getCheckoutDate());

        URI build = uriInfo.getAbsolutePathBuilder().path(reservation.getReservationNumber()).build();
        LOGGER.info(() -> "New reservation at " + build);
        return Response.created(build).build();
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

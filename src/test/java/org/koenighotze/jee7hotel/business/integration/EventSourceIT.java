package org.koenighotze.jee7hotel.business.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.jee7hotel.business.BookingService;
import org.koenighotze.jee7hotel.business.GuestService;
import org.koenighotze.jee7hotel.business.ReservationBackendHandler;
import org.koenighotze.jee7hotel.business.RoomService;
import org.koenighotze.jee7hotel.business.eventsource.EventSourceBean;
import org.koenighotze.jee7hotel.business.eventsource.LoggingEventSourceBean;
import org.koenighotze.jee7hotel.domain.Guest;
import org.koenighotze.jee7hotel.domain.Reservation;
import org.koenighotze.jee7hotel.domain.Room;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by dschmitz on 27.11.14.
 */
@RunWith(Arquillian.class)
public class EventSourceIT extends BaseArquillianSetup {

    private static final Logger LOGGER = Logger.getLogger(EventSourceIT.class.getName());

    @Inject
    private BookingService bookingService;

    @Inject
    private GuestService guestService;

    @Inject
    private RoomService roomService;

//    @Inject
//    private EventSourceBean eventSourceBean;

    @Deployment
    public static Archive<?> createMicroDeployment() {
        WebArchive baseDeployment = createBaseDeployment();

        baseDeployment.addPackages(true, "org.koenighotze.jee7hotel")
                .deleteClass(ReservationBackendHandler.class)
                .deleteClass(LoggingEventSourceBean.class);

        LOGGER.info(() -> baseDeployment.toString(Formatters.VERBOSE));

        return baseDeployment;
    }


    @Test
    public void testEventSourceEndToEnd() {
        // we'll create a reservation
        Reservation reservation = createReservation();
        assertThat(reservation, is(not(nullValue())));


        // we'll cancel the reservation
        this.bookingService.cancelReservation(reservation.getReservationNumber());

        // we'll reopen the reservation
        this.bookingService.reopenReservation(reservation.getReservationNumber());

        // now we'd expect three events to be added to the event store
//        assertThat(this.eventSourceBean.fetchAllEvents().size(), is(equalTo(3)));
    }

    private Reservation createReservation() {
        Guest guest = new Guest("My name", "foo@bar.de");
        this.guestService.saveGuest(guest);

        Room room = this.roomService.getAllRooms().get(0);

        return this.bookingService.bookRoom(guest, room, LocalDate.now(), LocalDate.now().plus(10, ChronoUnit.DAYS));
    }
}

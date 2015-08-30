

package org.koenighotze.jee7hotel.business.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.logging.Logger;
/**
 *
 * @author dschmitz
 */
@RunWith(Arquillian.class)
public class GuestServiceArquillianIntegrationIT {
    
    private static final Logger LOGGER = Logger.getLogger(GuestServiceArquillianIntegrationIT.class.getName());

//    @Inject
//    private GuestService guestService;
//
//    @Inject
//    private RoomService roomService;
//
//    @Inject
//    private BookingService bookingService;
//
//    @Inject
//    private Event<NewReservationEvent> reservationEvents;

    @Deployment
    public static Archive<?> createMicroDeployment() {
//        WebArchive baseDeployment = createBaseDeployment();
//
//        baseDeployment//.addPackages(true, BaseArquillianSetup::excludeTest, "org.koenighotze.jee7hotel")
//            .addClasses(GuestService.class, RoomService.class,
//                    BookingService.class, RestApplicationConfig.class,
//                    ReservationBackendHandler.class);
////            .deletePackage("org.koenighotze.jee7hotel.frontend")
//        LOGGER.info(() -> baseDeployment.toString(Formatters.VERBOSE));

//        return baseDeployment;
        return null;
    }
    
    @Test
    public void testSimpleScenario() {
//        assertNotNull(this.guestService);
//        List<Guest> guests = this.guestService.getAllGuests();
//        assertThat(guests, is(not(nullValue())));
    }

//    @Test
//    public void testAddGuestAndBookRoom() throws InterruptedException {
//        Guest guest = new Guest("My name", "foo@bar.de");
//        this.guestService.saveGuest(guest);
//
//        assertThat("Guest should have an Id", guest.getId(), is(not(nullValue())));
//        assertThat("Version should be set on guest", guest.getVersion(), is(not(nullValue())));
//
//        Room room = this.roomService.getAllRooms().get(0);
//
//        Reservation reservation = this.bookingService.bookRoom(guest, room, LocalDate.now(), LocalDate.now().plus(10, ChronoUnit.DAYS));
//
//        assertThat("A reservation should be created", reservation, is(not(nullValue())));
//        assertThat("Reservation should have an Id", reservation.getId(), is(not(nullValue())));
//        assertThat("Version should be set on reservation", reservation.getVersion(), is(not(nullValue())));
//        assertThat(reservation.getAssignedRoom(), is(equalTo(room)));
//        assertThat(reservation.getGuest(), is(equalTo(guest)));
//        assertThat("The reservation should be open", reservation.getReservationStatus(), is(equalTo(ReservationStatus.OPEN)));
//
//        // Wait for max time
//        LOGGER.info("Waiting for max allowed time to process booking");
//        Thread.sleep(11000);
//
//        // now the booking should be confirmed
//        Optional<Reservation> updatedReservation = this.bookingService.findReservationByNumber(reservation.getReservationNumber());
//        reservation = updatedReservation.get();
//        assertThat("The reservation should be confirmed", reservation.getReservationStatus(), is(equalTo(ReservationStatus.CONFIRMED)));
//    }
//
//    @Test
//    @RunAsClient
//    public void testRestInterface(@ArquillianResource URL baseURI) throws InterruptedException {
//        String targetUrl = baseURI + "rest/guest";
//
//        LOGGER.info("Looking up rest interface using " + targetUrl);
//
//        // Client does not implement autoclosable :(
//        Client client = null;
//        WebTarget webTarget = null;
//
//        try {
//            client = ClientBuilder.newClient();
//            webTarget = client.target(targetUrl);
//
//            Guest[] guests = webTarget
//                    .request()
//                    .get(Guest[].class);
//
//            assertThat("expected an array of guests", guests, is(not(nullValue())));
//            assertThat("expected one or more guests", guests.length, is(not(equalTo(0))));
//        }
//        finally {
//            client.close();
//        }
//    }
}


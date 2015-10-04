package org.koenighotze.jee7hotel.business.guest.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.jee7hotel.business.GuestService;
import org.koenighotze.jee7hotel.domain.Guest;

import javax.inject.Inject;
import java.io.File;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.formatter.Formatters.VERBOSE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.koenighotze.jee7hotel.business.guest.integration.BaseArquillianSetup.buildMavenLibraryDependencies;
import static org.koenighotze.jee7hotel.business.guest.integration.BaseArquillianSetup.readJBossWebXml;

/**
 * @author dschmitz
 */
@RunWith(Arquillian.class)
public class GuestServiceArquillianIntegrationIT {
    private static final Logger LOGGER = Logger.getLogger(GuestServiceArquillianIntegrationIT.class.getName());

    @Inject
    private GuestService guestService;

    @Deployment
    public static Archive<?> createMicroDeployment() {
        try {
            String jbossWebXml = readJBossWebXml(GuestServiceArquillianIntegrationIT.class.getPackage());

            File[] libs = buildMavenLibraryDependencies();

            WebArchive baseDeployment = create(WebArchive.class)
                    .addAsWebInfResource(jbossWebXml, "jboss-web.xml")
                    .addAsLibraries(libs);

            LOGGER.info(() -> baseDeployment.toString(VERBOSE));
            return baseDeployment;
        } catch (Exception e) {
            LOGGER.log(SEVERE, e, () -> "Creating deployment failed");
            throw e;
        }
    }

    @Test
    public void a_stored_guest_can_be_loaded_via_the_primary_key() {
        Guest guest = new Guest("foo", "name", "email");
        guestService.saveGuest(guest);

        assertNotNull(guest.getId());

        Guest loaded = guestService.findById(guest.getId()).get();
        assertEquals(loaded.getId(), guest.getId());
        assertEquals(loaded.getName(), guest.getName());
        assertEquals(loaded.getEmail(), guest.getEmail());
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





package org.koenighotze.jee7hotel.business.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.base.asset.AssetUtil;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.jee7hotel.business.BookingService;
import org.koenighotze.jee7hotel.business.GuestService;
import org.koenighotze.jee7hotel.business.RoomService;
import org.koenighotze.jee7hotel.business.events.NewReservationEvent;
import org.koenighotze.jee7hotel.domain.Guest;
import org.koenighotze.jee7hotel.domain.Reservation;
import org.koenighotze.jee7hotel.domain.ReservationStatus;
import org.koenighotze.jee7hotel.domain.Room;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.io.File;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 *
 * @author dschmitz
 */
@RunWith(Arquillian.class)
public class GuestServiceArquillianIntegrationIT {
    
    private static final Logger LOGGER = Logger.getLogger(GuestServiceArquillianIntegrationIT.class.getName());
    
    @Inject
    private GuestService guestService;

    @Inject
    private RoomService roomService;

    @Inject
    private BookingService bookingService;

    @Inject
    private Event<NewReservationEvent> reservationEvents;

    @Deployment
    public static Archive<?> createMicroDeployment() {
        // get special integration test persistence unit
        String persistenceXml = AssetUtil
                .getClassLoaderResourceName(GuestServiceArquillianIntegrationIT.class.getPackage(),
                "integration-test-persistence.xml");
        String loadSql = AssetUtil
                .getClassLoaderResourceName(GuestServiceArquillianIntegrationIT.class.getPackage(),
                        "integration-prepareTestData.sql");
        
        // build list of needed maven dependencies
        File[] deps = Maven.resolver().loadPomFromFile("pom.xml")
                .importRuntimeDependencies()
                .resolve()
                .withTransitivity()
                .asFile();
                        
        WebArchive archive = ShrinkWrap.create(WebArchive.class)
                // add demo and all recursive packages 
                // as of yet, we do not deploy the web layer   
                .addPackages(true, "org.koenighotze.jee7hotel.business", "org.koenighotze.jee7hotel.domain") 
                .addAsResource(persistenceXml, "META-INF/persistence.xml")
                .addAsResource(loadSql, "META-INF/load.sql")
                // this is not needed for jee7
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        
        for (File f : deps) {
            LOGGER.info(() -> "Add " + f.getName() + " as dependency");
            archive.addAsLibraries(f);
        }
        
        LOGGER.info(() -> archive.toString(Formatters.VERBOSE));
        return archive;
    }
    
    @Test
    public void testSimpleScenario() {
        assertNotNull(this.guestService);
        List<Guest> guests = this.guestService.getAllGuests();
        assertThat(guests, is(not(nullValue())));
    }

    @Test
    public void testAddGuestAndBookRoom() throws InterruptedException {
        Guest guest = new Guest("My name", "foo@bar.de");
        this.guestService.saveGuest(guest);

        assertThat("Guest should have an Id", guest.getId(), is(not(nullValue())));
        assertThat("Version should be set on guest", guest.getVersion(), is(not(nullValue())));

        Room room = this.roomService.getAllRooms().get(0);


        Reservation reservation = this.bookingService.bookRoom(guest, room, LocalDate.now(), LocalDate.now().plus(10, ChronoUnit.DAYS));

        assertThat("A reservation should be created", reservation, is(not(nullValue())));
        assertThat("Reservation should have an Id", reservation.getId(), is(not(nullValue())));
        assertThat("Version should be set on reservation", reservation.getVersion(), is(not(nullValue())));
        assertThat(reservation.getAssignedRoom(), is(equalTo(room)));
        assertThat(reservation.getGuest(), is(equalTo(guest)));
        assertThat("The reservation should be open", reservation.getReservationStatus(), is(equalTo(ReservationStatus.OPEN)));

        // Wait for max time
        LOGGER.info("Waiting for max allowed time to process booking");
        Thread.sleep(11000);

        // now the booking should be confirmed
        Optional<Reservation> updatedReservation = this.bookingService.findReservationByNumber(reservation.getReservationNumber());
        reservation = updatedReservation.get();
        assertThat("The reservation should be confirmed", reservation.getReservationStatus(), is(equalTo(ReservationStatus.CONFIRMED)));
    }
}



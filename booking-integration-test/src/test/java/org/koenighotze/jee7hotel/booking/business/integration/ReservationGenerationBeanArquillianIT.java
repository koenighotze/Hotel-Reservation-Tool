package org.koenighotze.jee7hotel.booking.business.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.jee7hotel.booking.business.BookingService;
import org.koenighotze.jee7hotel.booking.business.ReservationGenerationTriggerBean;
import org.koenighotze.jee7hotel.booking.business.events.BookingMessageTO;
import org.koenighotze.jee7hotel.booking.business.resources.MessagingDefinition;
import org.koenighotze.jee7hotel.booking.domain.Reservation;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;
import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.formatter.Formatters.VERBOSE;
import static org.junit.Assert.assertThat;
import static org.koenighotze.jee7hotel.booking.business.integration.BaseArquillianSetup.buildMavenLibraryDependencies;
import static org.koenighotze.jee7hotel.booking.business.integration.BaseArquillianSetup.readJBossWebXml;

/**
 * @author koenighotze
 */
@RunWith(Arquillian.class)
public class ReservationGenerationBeanArquillianIT {

    private static final Logger LOGGER = Logger.getLogger(ReservationGenerationBeanArquillianIT.class.getName());

    @Inject
    private BookingService bookingService;

    @Inject
    @JMSConnectionFactory("java:comp/DefaultJMSConnectionFactory")
    private JMSContext context;

    @Inject
    private ReservationGenerationTriggerBean reservationGenerationTriggerBean;

    // works in jboss
//    @Resource(lookup = "java:app/jee7hotel/reservationQueue")
    @Resource(lookup = MessagingDefinition.CLASSIC_QUEUE)
    private Queue queue;


    @Deployment
    public static Archive<?> createMicroDeployment() {
        try {
            String jbossWebXml = readJBossWebXml(ReservationGenerationBeanArquillianIT.class.getPackage());

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

//    @Deployment
//    public static WebArchive createDeployment() {
//        WebArchive baseDeployment = createBaseDeployment();
//
//        baseDeployment.addClass(ReservationGenerationBean.class)
//                .addClass(ReservationGenerationTriggerBean.class)
//                .addClass(BookingService.class)
//                .addClass(MessagingDefinition.class);
////                        .addClass(GuestService.class)
////                        .addClass(RoomService.class);
//
//        LOGGER.info(baseDeployment.toString(Formatters.VERBOSE));
//
//        return baseDeployment;
//    }

    @Test
    public void testMessageTriggersNewReservation() throws InterruptedException, JMSException {
//        Guest guest = this.guestService.getAllGuests().get(0);
//        Room room = this.roomService.getAllRooms().get(0);

        List<Reservation> reservationForGuest = this.bookingService.findReservationForGuest("123");

        BookingMessageTO messageTo = new BookingMessageTO(123L, "123", LocalDate.now(), LocalDate.now().plusDays(1));
        this.reservationGenerationTriggerBean.triggerReservation(messageTo);

        LOGGER.info("Waiting 3 seconds for message to be consumed");
        Thread.sleep(3000L);

        List<Reservation> afterTrigger = this.bookingService.findReservationForGuest("123");

        assertThat(afterTrigger.size(), is(equalTo(reservationForGuest.size() + 1)));
    }
}

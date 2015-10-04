package org.koenighotze.jee7hotel.booking.business.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.jee7hotel.booking.business.BookingService;
import org.koenighotze.jee7hotel.booking.business.ReservationGenerationTriggerBean;
import org.koenighotze.jee7hotel.booking.business.events.BookingMessageTO;
import org.koenighotze.jee7hotel.booking.domain.Reservation;

import javax.inject.Inject;
import javax.jms.JMSException;
import java.util.List;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;
import static java.time.LocalDate.now;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.koenighotze.jee7hotel.framework.integration.BaseArquillianSetup.createStandardDeployment;

/**
 * @author koenighotze
 */
@RunWith(Arquillian.class)
public class ReservationGenerationBeanArquillianIT {

    private static final Logger LOGGER = Logger.getLogger(ReservationGenerationBeanArquillianIT.class.getName());

    @Inject
    private BookingService bookingService;

    @Inject
    private ReservationGenerationTriggerBean reservationGenerationTriggerBean;

    @Deployment
    public static WebArchive createMicroDeployment() {
        return createStandardDeployment(ReservationGenerationBeanArquillianIT.class.getPackage());
    }

    @Test
    public void a_booking_message_triggers_a_reservation() throws InterruptedException, JMSException {
        List<Reservation> reservationForGuest = bookingService.findReservationForGuest("9999");

        assertFalse(reservationForGuest.isEmpty());

        BookingMessageTO messageTo = new BookingMessageTO(9999L, "999", now(), now().plusDays(1));
        reservationGenerationTriggerBean.triggerReservation(messageTo);

        LOGGER.info("Waiting max 11 seconds for message to be consumed");
        sleep(11000L);

        List<Reservation> afterTrigger = bookingService.findReservationForGuest("9999");

        assertThat(afterTrigger.size(), is(equalTo(reservationForGuest.size() + 1)));
    }
}

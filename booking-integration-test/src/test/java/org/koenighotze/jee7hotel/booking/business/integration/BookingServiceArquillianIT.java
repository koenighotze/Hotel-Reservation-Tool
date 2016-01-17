package org.koenighotze.jee7hotel.booking.business.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.jee7hotel.booking.business.BookingService;
import org.koenighotze.jee7hotel.framework.integration.BaseArquillianSetup;

import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * @author dschmitz
 */
@RunWith(Arquillian.class)
public class BookingServiceArquillianIT {
    private static final Logger LOGGER = Logger.getLogger(BookingServiceArquillianIT.class.getName());

    @Inject
    private BookingService bookingService;

    @Deployment
    public static Archive<?> createMicroDeployment() {
        return BaseArquillianSetup.createStandardDeployment(BookingServiceArquillianIT.class.getPackage());
    }

    @Test
    public void open_reservations_can_be_cancelled() {
        // todo
    }





}

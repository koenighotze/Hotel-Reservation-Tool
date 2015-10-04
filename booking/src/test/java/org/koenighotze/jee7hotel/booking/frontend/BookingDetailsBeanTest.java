package org.koenighotze.jee7hotel.booking.frontend;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.jee7hotel.booking.business.BookingService;
import org.koenighotze.jee7hotel.booking.domain.Reservation;
import org.koenighotze.jee7hotel.framework.test.AbstractBasePersistenceTest;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.faces.context.FacesContext;

import static java.math.BigDecimal.TEN;
import static java.time.LocalDate.now;
import static org.fest.assertions.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * @author dschmitz
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({FacesContext.class})
@PowerMockIgnore({"javax.management.*"})
public class BookingDetailsBeanTest extends AbstractBasePersistenceTest {

    @Mock
    private FacesContext mockFacesContext;

    @Override
    protected String getPersistenceUnitName() {
        return "booking-integration-test";
    }

    @Test
    public void an_open_reservation_can_be_cancelled() {
        mockStatic(FacesContext.class);

        FacesContext facesContext = mock(FacesContext.class);
        when(FacesContext.getCurrentInstance()).thenReturn(facesContext);

        BookingService bookingService = new BookingService();
        bookingService.setEntityManager(getEntityManager());
        BookingDetailsBean bean = new BookingDetailsBean(bookingService);

        Reservation reservation = new Reservation("guest", "number", "room", now(), now().plusDays(1), TEN);
        bean.setReservation(reservation);

        assertThat(bean.getReservation().isOpen());

        bean.cancel();

        assertThat(bean.getReservation().isCanceled());
    }
}
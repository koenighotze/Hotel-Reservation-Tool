package org.koenighotze.jee7hotel.booking.frontend;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * @author dschmitz
 */
public class NewReservationBeanTest {

    @Test
    public void the_booking_is_initialised_as_an_empty_booking() {
        NewReservationBean bean = new NewReservationBean();
        assertNotNull(bean.getBooking());
    }

}
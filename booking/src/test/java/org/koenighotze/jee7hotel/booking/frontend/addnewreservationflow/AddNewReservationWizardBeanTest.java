package org.koenighotze.jee7hotel.booking.frontend.addnewreservationflow;

import org.junit.Test;
import org.koenighotze.jee7hotel.business.AbstractBasePersistenceTest;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class AddNewReservationWizardBeanTest extends AbstractBasePersistenceTest {

//    private GuestService guestService;

//    private RoomService roomService;

    private AddNewReservationWizardBean wizardBean;

    @Override
    protected void initHook() {
        super.initHook();

//        this.guestService = new GuestService();
//        this.guestService.setEntityManager(getEntityManager());

//        this.roomService = new RoomService();
//        this.roomService.setEntityManager(getEntityManager());
        this.wizardBean = new AddNewReservationWizardBean();
//        this.wizardBean.setGuestService(this.guestService);
//        this.wizardBean.setRoomService(this.roomService);
    }
    @Override
    protected String getPersistenceUnitName() {
        return "booking-integration-test";
    }
    @Test
    public void testInit() throws Exception {

        wizardBean.init();

        assertThat(wizardBean.getGuestList(), is(not(nullValue())));

//        for (SelectItem s : wizardBean.getGuestList()) {
//            System.out.println(s.getLabel() + ", " + s.getValue());
//        }

//        for (SelectItem s : wizardBean.getRoomList()) {
//            System.out.println(s.getLabel() + ", " + s.getValue());
//        }

    }
}
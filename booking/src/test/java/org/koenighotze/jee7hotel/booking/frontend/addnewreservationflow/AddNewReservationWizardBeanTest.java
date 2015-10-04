package org.koenighotze.jee7hotel.booking.frontend.addnewreservationflow;

import mockit.Expectations;
import mockit.Mocked;
import org.junit.Test;
import org.koenighotze.jee7hotel.booking.business.BookingService;
import org.koenighotze.jee7hotel.booking.domain.GuestModel;
import org.koenighotze.jee7hotel.booking.persistence.GuestModelRepository;
import org.koenighotze.jee7hotel.framework.test.AbstractBasePersistenceTest;

import static java.util.Collections.singletonList;
import static org.fest.assertions.Assertions.assertThat;

public class AddNewReservationWizardBeanTest extends AbstractBasePersistenceTest {

    private AddNewReservationWizardBean wizardBean;

    @Mocked
    private BookingService bookingService;

    @Mocked
    private GuestModelRepository guestModelRepository;

    @Override
    protected void initHook() {
        super.initHook();

//        this.guestService = new GuestService();
//        this.guestService.setEntityManager(getEntityManager());

//        this.roomService = new RoomService();
//        this.roomService.setEntityManager(getEntityManager());
        this.wizardBean = new AddNewReservationWizardBean(bookingService, guestModelRepository);
//        this.wizardBean.setGuestService(this.guestService);
//        this.wizardBean.setRoomService(this.roomService);
    }

    @Override
    protected String getPersistenceUnitName() {
        return "booking-integration-test";
    }

    @Test
    public void after_initialization_the_list_of_guests_is_not_null() throws Exception {
        wizardBean.init();

        assertThat(wizardBean.getGuestList()).isNotNull();
    }

    @Test
    public void after_initialization_the_list_of_rooms_is_not_null() throws Exception {
        wizardBean.init();

        assertThat(wizardBean.getRoomList()).isNotNull();
    }

    @Test
    public void guests_entries_consist_of_name_and_public_id() throws Exception {
        new Expectations() {{
            guestModelRepository.findAllGuests();

            result = singletonList(new GuestModel("publicId", "lastName"));
        }};

        wizardBean.init();

        assertThat(wizardBean.getGuestList().get(0)).isEqualTo("lastName (publicId)");
    }
}
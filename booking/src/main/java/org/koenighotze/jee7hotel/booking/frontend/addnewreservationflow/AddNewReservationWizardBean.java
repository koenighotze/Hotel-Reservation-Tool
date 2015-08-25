

package org.koenighotze.jee7hotel.booking.frontend.addnewreservationflow;

import org.koenighotze.jee7hotel.booking.business.BookingService;
import org.koenighotze.jee7hotel.booking.domain.Reservation;
import org.koenighotze.jee7hotel.booking.domain.RoomEquipment;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.flow.FlowScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static org.koenighotze.jee7hotel.frontend.FacesMessageHelper.addFlashMessage;
import static org.koenighotze.jee7hotel.frontend.FacesMessageHelper.addMessage;

/**
 * @author dschmitz
 */
//@Named("addNewReservationWizardBean")
//why the hell do I have to use the 'old Annotation'?
@javax.faces.bean.ManagedBean(name = "addnewreservationflow")
@FlowScoped("addnewreservationflow")
public class AddNewReservationWizardBean implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(AddNewReservationWizardBean.class.getName());

    @Inject
    private BookingService bookingService;

    private List<String> guestList;
    private List<String> roomList;
    private LocalDate checkoutDate;
    private LocalDate checkinDate;

    private String selectedGuest;
    private String selectedRoom;
    private String room;
    private String guest;

    private List<RoomEquipment> roomEquipments = Arrays.asList(RoomEquipment.values());
    private RoomEquipment selectedRoomEquipment;

    @PostConstruct
    public void init() {
        this.guestList = Arrays.asList("a", "b", "c");
        this.roomList = Arrays.asList("001", "002", "003");// this.roomService

        this.checkinDate = LocalDate.now().plusDays(1);
        this.checkoutDate = LocalDate.now().plusDays(2);
    }

    public List<String> getGuestList() {
        return guestList;
    }

    public String confirmBooking() {
        if (null == this.checkinDate) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Checkin date is mandatory");
            return null;
        }

        if (null == this.checkoutDate) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Checkout date is mandatory");
            return null;
        }

        if (null == this.guest) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Guest is mandatory");
            return null;
        }

        if (null == this.room) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Room is mandatory");
            return null;
        }

        Reservation reservation = this.bookingService.bookRoom(this.guest, this.room, this.checkinDate, this.checkoutDate);
        addFlashMessage(FacesMessage.SEVERITY_INFO, "Room "
                + reservation.getAssignedRoom()//.getRoomNumber()
                + " booked for "
                + reservation.getGuest()
                + "; Reservation number "
                + reservation.getReservationNumber()
                + " Costs: "
                + reservation.getCostsInEuro() + " EUR");
        return String.format("/booking/booking.xhtml?reservationNumber=%s&faces-redirect=true", reservation.getReservationNumber());
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckinDate(@NotNull LocalDate checkinDate) {
        this.checkinDate = checkinDate;
    }

    public void setCheckoutDate(@NotNull LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public LocalDate getCheckinDate() {
        return checkinDate;
    }

    public void setSelectedGuest(@NotNull String selectedGuest) {
        LOGGER.info("Setting guest to " + selectedGuest);
        this.selectedGuest = selectedGuest;

//        if (null != this.selectedGuest) {
//            this.guest = this.guestService.findById(this.selectedGuest).orElse(new Guest("", ""));
//        }
    }

    public List<RoomEquipment> getRoomEquipments() {
        return this.roomEquipments;
    }

    public String getSelectedGuest() {
        return selectedGuest;
    }

    public void setSelectedRoom(String selectedRoom) {
        this.selectedRoom = selectedRoom;
//        if (null != this.selectedRoom) {
//            this.room = this.roomService
//                    .findRoomById(this.selectedRoom)
//                    .orElse(new Room("", RoomEquipment.BUDGET));
//        }
    }

    public String getSelectedRoom() {
        return selectedRoom;
    }

    public List<String> getRoomList() {
        return this.roomList; // TODO filter based on criteria
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(@NotNull String room) {
        this.room = room;
    }

    public String getGuest() {
        return guest;
    }

    public void setGuest(@NotNull String guest) {
        this.guest = guest;
    }

    public void setSelectedRoomEquipment(@NotNull RoomEquipment roomEquipment) {
        this.selectedRoomEquipment = roomEquipment;
    }

    public RoomEquipment getSelectedRoomEquipment() {
        return selectedRoomEquipment;
    }
}

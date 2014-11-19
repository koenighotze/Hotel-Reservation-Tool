

package org.koenighotze.jee7hotel.frontend.addnewreservationflow;

import org.koenighotze.jee7hotel.business.BookingService;
import org.koenighotze.jee7hotel.business.GuestService;
import org.koenighotze.jee7hotel.business.RoomService;
import org.koenighotze.jee7hotel.domain.Guest;
import org.koenighotze.jee7hotel.domain.Reservation;
import org.koenighotze.jee7hotel.domain.Room;
import org.koenighotze.jee7hotel.domain.RoomEquipment;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.flow.FlowScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static org.koenighotze.jee7hotel.frontend.FacesMessageHelper.addFlashMessage;
import static org.koenighotze.jee7hotel.frontend.FacesMessageHelper.addMessage;
/**
 *
 * @author dschmitz
 */
@Named("addNewReservationWizardBean")
@FlowScoped("addnewreservationflow") 
public class AddNewReservationWizardBean implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(AddNewReservationWizardBean.class.getName());

    @Inject
    private GuestService guestService;

    @Inject
    private RoomService roomService;

    @Inject
    private BookingService bookingService;

    private List<Guest> guestList;
    private List<Room> roomList;
    private LocalDate checkoutDate;
    private LocalDate checkinDate;

    private Long selectedGuest;
    private Long selectedRoom;
    private Room room;
    private Guest guest;

    private List<RoomEquipment> roomEquipments = Arrays.asList(RoomEquipment.values());
    private RoomEquipment selectedRoomEquipment;

    public void setGuestService(GuestService guestService) {
        this.guestService = guestService;
    }

    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostConstruct
    public void init() {
        this.guestList = this.guestService
                .getAllGuests();

        this.roomList = this.roomService
                .getAllRooms();

        this.checkinDate = LocalDate.now().plusDays(1);
        this.checkoutDate = LocalDate.now().plusDays(2);
    }

    public List<Guest> getGuestList() {
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
                + reservation.getAssignedRoom().getRoomNumber()
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

    public void setSelectedGuest(@NotNull Long selectedGuest) {
        LOGGER.info("Setting guest to " + selectedGuest);
        this.selectedGuest = selectedGuest;

        if (null != this.selectedGuest) {
            this.guest = this.guestService.findById(this.selectedGuest).orElse(new Guest("", ""));
        }
    }

    public List<RoomEquipment> getRoomEquipments() {
        return this.roomEquipments;
    }

    public Long getSelectedGuest() {
        return selectedGuest;
    }

    public void setSelectedRoom(Long selectedRoom) {
        this.selectedRoom = selectedRoom;
        if (null != this.selectedRoom) {
            this.room = this.roomService
                    .findRoomById(this.selectedRoom)
                    .orElse(new Room("", RoomEquipment.BUDGET));
        }
    }

    public Long getSelectedRoom() {
        return selectedRoom;
    }

    public List<Room> getRoomList() {
        return this.roomList; // TODO filter based on criteria
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(@NotNull Room room) {
        this.room = room;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(@NotNull Guest guest) {
        this.guest = guest;
    }

    public void setSelectedRoomEquipment(@NotNull RoomEquipment roomEquipment) {
        this.selectedRoomEquipment = roomEquipment;
    }

    public RoomEquipment getSelectedRoomEquipment() {
        return selectedRoomEquipment;
    }

}

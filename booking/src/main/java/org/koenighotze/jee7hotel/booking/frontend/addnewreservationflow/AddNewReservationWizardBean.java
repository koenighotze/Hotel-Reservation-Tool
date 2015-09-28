

package org.koenighotze.jee7hotel.booking.frontend.addnewreservationflow;

import org.koenighotze.jee7hotel.booking.business.BookingService;
import org.koenighotze.jee7hotel.booking.domain.Reservation;
import org.koenighotze.jee7hotel.booking.domain.RoomEquipment;
import org.koenighotze.jee7hotel.booking.persistence.GuestModelRepository;

import javax.annotation.PostConstruct;
import javax.faces.flow.FlowScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.time.LocalDate.now;
import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;
import static java.util.stream.IntStream.rangeClosed;
import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;
import static org.koenighotze.jee7hotel.frontend.FacesMessageHelper.addFlashMessage;
import static org.koenighotze.jee7hotel.frontend.FacesMessageHelper.addMessage;

/**
 * Flow Bean for the booking wizard.
 *
 * @author dschmitz
 */
@Named("addNewReservationWizardBean")
@FlowScoped("addnewreservationflow")
public class AddNewReservationWizardBean implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(AddNewReservationWizardBean.class.getName());

    private BookingService bookingService;

    private GuestModelRepository guestModelRepository;

    private List<String> guestList;
    private List<String> roomList;
    private LocalDate checkoutDate;
    private LocalDate checkinDate;

    private String selectedGuest;
    private String selectedRoom;

    private List<RoomEquipment> roomEquipments = asList(RoomEquipment.values());
    private RoomEquipment selectedRoomEquipment;

    public AddNewReservationWizardBean() {
    }

    @Inject
    public AddNewReservationWizardBean(BookingService bookingService, GuestModelRepository guestModelRepository) {
        this.bookingService = bookingService;
        this.guestModelRepository = guestModelRepository;
    }

    @PostConstruct
    public void init() {
        LOGGER.info("Constructing initial data for wizard");

        guestList = guestModelRepository.findAllGuests().stream().map(guest -> format("%s (%s)", guest.getName(), guest.getPublicId())).collect(Collectors.toList());

        this.roomList = new ArrayList<>();
        rangeClosed(0, 10).forEach(i -> this.roomList.add(randomUUID().toString()));

        this.checkinDate = now().plusDays(1);
        this.checkoutDate = now().plusDays(2);
    }

    public List<String> getGuestList() {
        return guestList;
    }

    public String confirmBooking() {
        if (null == this.checkinDate) {
            addMessage(SEVERITY_ERROR, "Checkin date is mandatory");
            return null;
        }

        if (null == this.checkoutDate) {
            addMessage(SEVERITY_ERROR, "Checkout date is mandatory");
            return null;
        }

        if (null == this.selectedGuest) {
            addMessage(SEVERITY_ERROR, "Guest is mandatory");
            return null;
        }

        if (null == this.selectedRoom) {
            addMessage(SEVERITY_ERROR, "Room is mandatory");
            return null;
        }

        Reservation reservation = this.bookingService.bookRoom(this.selectedGuest, this.selectedRoom, this.checkinDate, this.checkoutDate);
        addFlashMessage(SEVERITY_INFO, format("Room %s booked for %s; Reservation number %s  Costs: %s EUR",
                reservation.getAssignedRoom(), reservation.getGuest(), reservation.getReservationNumber(), reservation.getCostsInEuro()));
        return "/bookings.xhtml";
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckinDate(@NotNull LocalDate checkinDate) {
        LOGGER.info("Setting checkin to " + checkinDate);

        this.checkinDate = checkinDate;
    }

    public void setCheckoutDate(@NotNull LocalDate checkoutDate) {
        LOGGER.info("Setting checkout to " + checkoutDate);
        this.checkoutDate = checkoutDate;
    }

    public LocalDate getCheckinDate() {
        return checkinDate;
    }

    public void setSelectedGuest(@NotNull String selectedGuest) {
        LOGGER.info("Setting guest to " + selectedGuest);
        this.selectedGuest = selectedGuest;
    }

    public List<RoomEquipment> getRoomEquipments() {
        return this.roomEquipments;
    }

    public String getSelectedGuest() {
        return selectedGuest;
    }

    public void setSelectedRoom(@NotNull String selectedRoom) {
        LOGGER.info("Setting room to " + selectedRoom);
        this.selectedRoom = selectedRoom;
    }

    public String getSelectedRoom() {
        return selectedRoom;
    }

    public List<String> getRoomList() {
        return this.roomList; // TODO filter based on criteria
    }

    public void setSelectedRoomEquipment(@NotNull RoomEquipment roomEquipment) {
        LOGGER.info("Setting roomequipment to " + roomEquipment);
        this.selectedRoomEquipment = roomEquipment;
    }

    public RoomEquipment getSelectedRoomEquipment() {
        return selectedRoomEquipment;
    }
}

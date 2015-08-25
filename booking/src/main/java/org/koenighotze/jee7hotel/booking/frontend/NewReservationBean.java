package org.koenighotze.jee7hotel.booking.frontend;

import org.koenighotze.jee7hotel.booking.business.BookingService;
import org.koenighotze.jee7hotel.booking.domain.Reservation;
import org.koenighotze.jee7hotel.booking.frontend.model.Booking;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author dschmitz
 */
@Named
@ViewScoped
public class NewReservationBean implements Serializable {
    private String guestId;

    private String roomId;

    private Booking booking = new Booking();

    @Inject
    private BookingService bookingService;

    public Booking getBooking() {
        return this.booking;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
//        if (null != this.guestId) {
//            Optional<Guest> guest = this.guestService.findById(this.guestId);
//
//            this.booking.setGuest(guest.orElseGet(() -> {
//                FacesMessageHelper.addMessage("welcomeForm:guest_id:inputTextField", FacesMessage.SEVERITY_ERROR,
//                        "Cannot find guest " + this.guestId, "");
//                return null;
//            }));
//        }
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void init(ComponentSystemEvent evt) {
//        Room room = new Room("", RoomEquipment.BUDGET);
//        if (null != this.roomId) {
//            room = this.roomService.findRoomById(this.roomId)
//                    .orElseGet(() -> {
//                        FacesMessageHelper.addMessage("welcomeForm:room_number:inputTextField",
//                                FacesMessage.SEVERITY_ERROR,
//                                "Cannot find room " + this.roomId, "");
//                        return new Room("", RoomEquipment.BUDGET);
//                    });
//        }

//        Guest guest = new Guest("", "");
//        if (null != this.guestId) {
//            guest = this.guestService.findById(this.guestId)
//                    .orElseGet(() -> {
//                        FacesMessageHelper.addMessage("welcomeForm:guest_id:inputTextField", FacesMessage.SEVERITY_ERROR,
//                                "Cannot find guest " + this.guestId, "");
//                        return new Guest("", "");
//                    });
//        }

        this.booking = new Booking();
        this.booking.setGuest(guestId + "");
        this.booking.setRoom(roomId);
        this.booking.setCheckinDate(LocalDate.now().plusDays(1));
        this.booking.setCheckoutDate(LocalDate.now().plusDays(2));
    }

    public void setRoomNumber(String number) {
//        Optional<Room> room = this.roomService.findRoomByNumber(number);

//        if (!room.isPresent()) {
//            FacesMessageHelper.addMessage("welcomeForm:room_number:inputTextField",
//                    FacesMessage.SEVERITY_ERROR, "Cannot find room with number " + number, "");
//            return;
//        }
        this.roomId = number; //room.get().getId();
        this.booking.setRoom(number); //room.get());
    }

    public String getRoomNumber() {
        if (null == this.booking.getRoom()) {
            return null;
        }

        return this.booking.getRoom();//.getRoomNumber();
    }

    public String addReservation() {
        Reservation realReservation
                = this.bookingService.bookRoom(this.booking.getGuest(), this.booking.getRoom(),
                this.booking.getCheckinDate(), this.booking.getCheckoutDate());

        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.setKeepMessages(true);

        FacesMessage message = new FacesMessage("Room "
                + this.booking.getRoom()//.getRoomNumber()
                + " booked for "
                + this.booking.getGuest()
                + "; Reservation number "
                + realReservation.getReservationNumber()
                + " Costs: "
                + realReservation.getCostsInEuro() + " EUR");
        FacesContext.getCurrentInstance().addMessage(null, message);
        this.booking = null;

        return String.format("/booking/booking.xhtml?reservationNumber=%s&faces-redirect=true", realReservation.getReservationNumber());
    }
}

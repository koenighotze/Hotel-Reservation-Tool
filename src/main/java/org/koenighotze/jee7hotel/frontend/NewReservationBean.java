

package org.koenighotze.jee7hotel.frontend;

import org.koenighotze.jee7hotel.business.BookingService;
import org.koenighotze.jee7hotel.business.GuestService;
import org.koenighotze.jee7hotel.business.RoomService;
import org.koenighotze.jee7hotel.domain.Guest;
import org.koenighotze.jee7hotel.domain.Reservation;
import org.koenighotze.jee7hotel.domain.Room;
import org.koenighotze.jee7hotel.domain.RoomEquipment;
import org.koenighotze.jee7hotel.frontend.model.Booking;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Optional;

import static org.koenighotze.jee7hotel.frontend.FacesMessageHelper.addMessage;
/**
 *
 * @author dschmitz
 */
@Named
@ViewScoped
public class NewReservationBean implements Serializable {
    private Long guestId;

    private Long roomId;

    private Booking booking = new Booking();

    @Inject
    private BookingService bookingService;

    @Inject
    private RoomService roomService;

    @Inject
    private GuestService guestService;

    public Booking getBooking() {
        return this.booking;
    }

    public Long getGuestId() {
        return guestId;
    }

    public void setGuestId(Long guestId) {
        this.guestId = guestId;
        if (null != this.guestId) {
            Optional<Guest> guest = this.guestService.findById(this.guestId);

            this.booking.setGuest(guest.orElseGet(() -> {
                addMessage("welcomeForm:guest_id:inputTextField", FacesMessage.SEVERITY_ERROR,
                        "Cannot find guest " + this.guestId, "");
                return null;
            }));
        }
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public void init(ComponentSystemEvent evt) {
        Room room = new Room("", RoomEquipment.BUDGET);
        if (null != this.roomId) {
            room = this.roomService.findRoomById(this.roomId)
                    .orElseGet(() -> {
                        addMessage("welcomeForm:room_number:inputTextField",
                                FacesMessage.SEVERITY_ERROR,
                                "Cannot find room " + this.roomId, "");
                        return new Room("", RoomEquipment.BUDGET);
                    });
        }

        Guest guest = new Guest("", "");
        if (null != this.guestId) {
            guest = this.guestService.findById(this.guestId)
                    .orElseGet(() -> {
                        addMessage("welcomeForm:guest_id:inputTextField", FacesMessage.SEVERITY_ERROR,
                                "Cannot find guest " + this.guestId, "");
                        return new Guest("", "");
                    });
        }

        this.booking = new Booking();
        this.booking.setGuest(guest);
        this.booking.setRoom(room);
        this.booking.setCheckinDate(LocalDate.now().plusDays(1));
        this.booking.setCheckoutDate(LocalDate.now().plusDays(2));
    }

    public void setRoomNumber(String number) {
        Optional<Room> room = this.roomService.findRoomByNumber(number);

        if (!room.isPresent()) {
            addMessage("welcomeForm:room_number:inputTextField",
                    FacesMessage.SEVERITY_ERROR, "Cannot find room with number " + number, "");
            return;
        }
        this.roomId = room.get().getId();
        this.booking.setRoom(room.get());
    }

    public String getRoomNumber() {
        if (null == this.booking.getRoom()) {
            return null;
        }

        return this.booking.getRoom().getRoomNumber();
    }



    public String addReservation() {
        Reservation realReservation
                = this.bookingService.bookRoom(this.booking.getGuest(), this.booking.getRoom(),
                this.booking.getCheckinDate(), this.booking.getCheckoutDate());


        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.setKeepMessages(true);




        FacesMessage message = new FacesMessage("Room "
                + this.booking.getRoom().getRoomNumber()
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

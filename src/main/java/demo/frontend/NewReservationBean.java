

package demo.frontend;

import demo.business.BookingService;
import demo.business.GuestService;
import demo.business.RoomService;
import demo.domain.Guest;
import demo.domain.Reservation;
import demo.domain.Room;
import demo.domain.RoomEquipment;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

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
            Guest guest = this.guestService.findById(this.guestId);
            if (null == guest) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, 
                                "Cannot find guest " + this.guestId, null));
            }
            this.booking.setGuest(guest);
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
            room = this.roomService.findRoomById(this.roomId);
            if (null == room) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                                "Cannot find room " + this.roomId, null));
            }
        }
        
        Guest guest = new Guest("", "");
        if (null != this.guestId) {
            guest = this.guestService.findById(this.guestId);
            if (null == guest) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                                "Cannot find guest " + this.guestId, null));
            }
        }
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        
        Date tomorrow = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date twoDaysAhead = cal.getTime();
        
        this.booking = new Booking();
        this.booking.setGuest(guest);
        this.booking.setRoom(room);
        this.booking.setCheckinDate(tomorrow);
        this.booking.setCheckoutDate(twoDaysAhead);
    }
    
    public void setRoomNumber(String number) {
        Room room = this.roomService.findRoomByNumber(number);
        if (null == room) {
            FacesMessage message 
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot find room with number " + number, "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        this.roomId = room.getId();
        this.booking.setRoom(room);
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

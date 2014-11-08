package demo.domain;

import java.math.BigDecimal;
import java.util.Date;
import javax.enterprise.event.Event;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a simple reservation.
 *
 * Each guest may have one or many reservations.
 *
 * @author dschmitz
 */
@Entity
@XmlRootElement
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne
    private Guest guest;
   
    @NotNull
    private String reservationNumber;

    // TODO: move to jdk 1.8 and use new date abstractions
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date checkinDate;

    @Temporal(TemporalType.DATE)
    @NotNull
    private Date checkoutDate;
    
    @OneToOne(optional = true)
    private Room assignedRoom;

    @Convert(converter = ReservationStatusConverter.class)
    private ReservationStatus reservationStatus = ReservationStatus.OPEN;
    
    @NotNull
    private BigDecimal costsInEuro;
    
    @Version
    private Long version;
    
  
   
    
    Reservation() {        
    }
    
    public Reservation(Guest guest, String reservationNumber, Room room, Date checkinDate, Date checkoutDate, BigDecimal costs) {
        this.guest = guest;
        this.assignedRoom = room;
        this.reservationNumber = reservationNumber;
        this.checkinDate = new Date(checkinDate.getTime());
        this.checkoutDate = new Date(checkoutDate.getTime());
        this.costsInEuro = costs;
    }

    public BigDecimal getCostsInEuro() {
        return costsInEuro;
    }

    public void setCostsInEuro(BigDecimal costsInEuro) {
        this.costsInEuro = costsInEuro;
    }

    
    public Long getId() {
        return id;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public Date getCheckinDate() {
        return new Date(checkinDate.getTime());
    }

    public Date getCheckoutDate() {
        return new Date(checkoutDate.getTime());
    }

    
    public Room getAssignedRoom() {
        return assignedRoom;
    }

    public void setAssignedRoom(Room assignedRoom) {
        this.assignedRoom = assignedRoom;
    }

    public Guest getGuest() {
        return guest;
    }

    public Long getVersion() {
        return version;
    }

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }
    
    public boolean isOpen() {
        return ReservationStatus.OPEN == this.reservationStatus;
    }

    public boolean isCanceled() {
        return ReservationStatus.CANCELED == this.reservationStatus;
    }

    
    @Override
    public String toString() {
        return "Reservation{" + "id=" + id + ", guest=" + guest + ", reservationNumber=" + reservationNumber + ", checkinDate=" + checkinDate + ", checkoutDate=" + checkoutDate + ", assignedRoom=" + assignedRoom + ", version=" + version + '}';
    }
    
    

}

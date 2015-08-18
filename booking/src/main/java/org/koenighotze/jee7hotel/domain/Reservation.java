package org.koenighotze.jee7hotel.domain;

import org.koenighotze.jee7hotel.domain.converter.LocalDateConverter;
import org.koenighotze.jee7hotel.domain.converter.ReservationStatusConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents a simple reservation.
 * <p>
 * Each guest may have one or many reservations.
 *
 * @author dschmitz
 */
@Entity
@XmlRootElement
@NamedQueries(
        {
        @NamedQuery(
                name = "Reservation.findByGuest",
                query = "select r from Reservation r where r.guestId = :guest"
        ),
        @NamedQuery(
                name = "Reservation.findByReservationNumber",
                query = "select r from Reservation r where r.reservationNumber = :number"
        )}
)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
//    @OneToOne
    // TODO
    private String guestId;

    @NotNull
    private String reservationNumber;

    //@Temporal(TemporalType.)
    @NotNull
    @Convert(converter = LocalDateConverter.class)
    private LocalDate checkinDate;

    ///@Temporal(TemporalType.DATE)
    @NotNull
    @Convert(converter = LocalDateConverter.class)
    private LocalDate checkoutDate;

    // TODO
//    @OneToOne(optional = true)
    private String assignedRoomId;

    @Convert(converter = ReservationStatusConverter.class)
    private ReservationStatus reservationStatus = ReservationStatus.OPEN;

    @NotNull
    private BigDecimal costsInEuro;

    @Version
    private Long version;

    Reservation() {
    }

    public Reservation(String guest, String reservationNumber, String room, LocalDate checkinDate, LocalDate checkoutDate, BigDecimal costs) {
        this.guestId = guest;
        this.assignedRoomId = room;
        this.reservationNumber = reservationNumber;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
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

    public LocalDate getCheckinDate() {
        return this.checkinDate;
    }

    public LocalDate getCheckoutDate() {
        return this.checkoutDate;
    }


    public String getAssignedRoom() {
        return assignedRoomId;
    }

    public void setAssignedRoom(String assignedRoom) {
        this.assignedRoomId = assignedRoom;
    }

    public String getGuest() {
        return guestId;
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
        return "Reservation{" + "id=" + id + ", guest=" + guestId + ", reservationNumber=" + reservationNumber + ", checkinDate=" + checkinDate + ", checkoutDate=" + checkoutDate + ", assignedRoom=" + assignedRoomId + ", version=" + version + '}';
    }


}

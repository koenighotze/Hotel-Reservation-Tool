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
                query = "select r from Reservation r where r.guest = :guest"
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
    @OneToOne
    private Guest guest;

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

    public Reservation(Guest guest, String reservationNumber, Room room, LocalDate checkinDate, LocalDate checkoutDate, BigDecimal costs) {
        this.guest = guest;
        this.assignedRoom = room;
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

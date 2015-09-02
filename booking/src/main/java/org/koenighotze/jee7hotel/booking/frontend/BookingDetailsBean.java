

package org.koenighotze.jee7hotel.booking.frontend;

import org.koenighotze.jee7hotel.booking.business.BookingService;
import org.koenighotze.jee7hotel.booking.domain.Reservation;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.logging.Logger;

import static javax.faces.application.FacesMessage.SEVERITY_INFO;
import static org.koenighotze.jee7hotel.frontend.FacesMessageHelper.addMessage;

/**
 * Handles showing details of a booking.
 *
 * @author dschmitz
 */
@Named
@ViewScoped
public class BookingDetailsBean implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(BookingDetailsBean.class.getName());

    private String reservationNumber;

    private Reservation reservation;

    private BookingService bookingService;

    public BookingDetailsBean() {
    }

    @Inject
    public BookingDetailsBean(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostConstruct
    public void init() {
        this.reservation = null;
        if (this.reservationNumber == null) {
            return;
        }

        LOGGER.info(() -> "Loading reservation " + reservationNumber);

        this.bookingService.findReservationByNumber(this.reservationNumber)
                .ifPresent(r -> this.reservation = r);
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(@NotNull String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public void reopen() {
        this.bookingService.reopenReservation(this.reservation.getReservationNumber());

        addMessage(null, SEVERITY_INFO,
                "Reservation " + this.reservationNumber + " is reopened!", "");
    }

    public void cancel() {
        this.bookingService.cancelReservation(this.reservation.getReservationNumber());

        addMessage(null, SEVERITY_INFO,
                "Reservation " + this.reservationNumber + " is canceled!", "");
    }
}

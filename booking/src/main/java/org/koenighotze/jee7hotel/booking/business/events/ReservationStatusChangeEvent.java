

package org.koenighotze.jee7hotel.booking.business.events;

import org.koenighotze.jee7hotel.booking.domain.ReservationStatus;

import javax.validation.constraints.NotNull;

/**
 * @author dschmitz
 */
public class ReservationStatusChangeEvent {
    private final String reservationNumber;
    private final ReservationStatus oldState;
    private final ReservationStatus newState;

    public ReservationStatusChangeEvent(@NotNull String reservationNumber, ReservationStatus oldState, @NotNull ReservationStatus newState) {
        this.reservationNumber = reservationNumber;
        this.oldState = oldState;
        this.newState = newState;
    }

    public ReservationStatus getOldState() {
        return oldState;
    }

    public ReservationStatus getNewState() {
        return newState;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    @Override
    public String toString() {
        return "ReservationStatusChangeEvent{" + "reservationNumber=" + reservationNumber + ", oldState=" + oldState + ", newState=" + newState + '}';
    }
}

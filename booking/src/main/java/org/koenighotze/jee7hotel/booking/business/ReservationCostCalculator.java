package org.koenighotze.jee7hotel.booking.business;

import org.koenighotze.jee7hotel.booking.domain.RoomEquipment;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.math.BigDecimal;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * @author dschmitz
 */
@Named
@ApplicationScoped
public class ReservationCostCalculator {
    // TODO: extract to calculation strategy or similar
    public BigDecimal calculateRateFor(RoomEquipment roomEquipment, LocalDate checkin, LocalDate checkout) {
        // rather simple...
        long days = checkin.until(checkout, DAYS);

        BigDecimal rate = null;

        switch (roomEquipment) {
            case BUDGET:
                rate = BigDecimal.valueOf(60L);
                break;
            case STANDARD:
                rate = BigDecimal.valueOf(92L);
                break;
            case SUPERIOR:
                rate = BigDecimal.valueOf(210L);
                break;
            default:
                throw new IllegalArgumentException("Unknown equipment " + roomEquipment);
        }

        return rate.multiply(BigDecimal.valueOf(days));
    }

}

package org.koenighotze.jee7hotel.booking.business;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.time.LocalDate.of;
import static java.time.Month.SEPTEMBER;
import static org.fest.assertions.Assertions.assertThat;
import static org.koenighotze.jee7hotel.booking.domain.RoomEquipment.BUDGET;

/**
 * @author dschmitz
 */
public class ReservationCostCalculatorTest {

    @Test
    public void calcRate() {
        LocalDate from = of(1976, SEPTEMBER, 8);
        LocalDate to = of(1976, SEPTEMBER, 12);
        BigDecimal rate = new ReservationCostCalculator().calculateRateFor(BUDGET, from, to);
        assertThat(rate).isEqualTo(BigDecimal.valueOf(240L));
    }
}
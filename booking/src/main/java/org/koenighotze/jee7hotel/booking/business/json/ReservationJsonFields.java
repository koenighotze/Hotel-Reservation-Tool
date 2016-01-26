package org.koenighotze.jee7hotel.booking.business.json;

/**
 * @author dschmitz
 */
public enum ReservationJsonFields {
    GUESTID("guestId"),
    RESERVATIONNUMBER("reservationNumber"),
    CHECKINDATE("checkinDate"),
    CHECKOUTDATE("checkoutDate"),
    ASSIGNEDROOMID("assignedRoomId"),
    RESERVATIONSTATUS("reservationStatus"),
    COSTSINEURO("costsInEuro");

    private final String fieldName;

    ReservationJsonFields(String field) {
        this.fieldName = field;
    }

    public String fieldName() {
        return this.fieldName;
    }
}

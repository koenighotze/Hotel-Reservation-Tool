package org.koenighotze.jee7hotel.booking.domain;

/**
 * Represents the equipment level of a room.
 * 
 * @author dschmitz
 */
// TODO: must be removed
public enum RoomEquipment {
    /**
     * Shower only, no TV
     */
    BUDGET,
    /**
     * Shower only, TV, paid Mini Bar
     */
    STANDARD,
    /**
     * Shower and Bath, TV, Mini Bar included , Coffee
     */
    SUPERIOR
}

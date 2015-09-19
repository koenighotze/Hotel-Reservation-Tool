package org.koenighotze.jee7hotel.domain;

/**
 * Represents the equipment level of a room.
 * 
 * @author dschmitz
 */
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

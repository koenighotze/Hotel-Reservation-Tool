package org.koenighotze.jee7hotel.booking.business.events;

import org.koenighotze.jee7hotel.booking.business.json.BookingMessageTOReader;
import org.koenighotze.jee7hotel.booking.business.json.BookingMessageTOWriter;

import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

/**
 * @author koenighotze
 */
public class BookingMessageTO {
    private Long guest;
    private String room;
    private LocalDate checkin;
    private LocalDate checkout;

    public BookingMessageTO(Long guest, String room, LocalDate checkin, LocalDate checkout) {
        this.guest = guest;
        this.room = room;
        this.checkin = checkin;
        this.checkout = checkout;
    }

    public Long getGuest() {
        return guest;
    }

    public String getRoom() {
        return room;
    }

    public LocalDate getCheckin() {
        return checkin;
    }

    public LocalDate getCheckout() {
        return checkout;
    }


    @Override
    public String toString() {
        return "BookingMessageTO{" +
                "guest=" + guest +
                ", room='" + room + '\'' +
                ", checkin=" + checkin +
                ", checkout=" + checkout +
                '}';
    }

    public static BookingMessageTO fromJson(String json) {
        try (ByteArrayInputStream bos = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))) {
            return new BookingMessageTOReader().readFrom(null, null, null, null, null, bos);
        } catch (IOException e) {
            throw new RuntimeException("Cannot convert " + json, e);
        }


//        JsonReader reader = Json.createReader(new StringReader(json));
//
//        JsonObject jsonObject = reader.readObject();
//        Long guestId = jsonObject.getJsonNumber("guestId").longValue();
//        String roomNumber = jsonObject.getString("roomNumber");
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//        LocalDate checkin = LocalDate.parse(jsonObject.getString("checkin"), formatter);
//        LocalDate checkout = LocalDate.parse(jsonObject.getString("checkout"), formatter);
//        return new BookingMessageTO(guestId, roomNumber, checkin, checkout);
    }


    public String toJson() {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            new BookingMessageTOWriter().writeTo(this, null, null, null, MediaType.APPLICATION_JSON_TYPE, null, bos);
            bos.flush();
            return bos.toString(StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            throw new RuntimeException("Cannot convert " + this + " to JSON", e);
        }


//        StringWriter sw = new StringWriter();
//        JsonGenerator generator = Json.createGenerator(sw);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        generator.writeStartObject()
//                .write("guestId", this.guest)
//                .write("roomNumber", this.room)
//                .write("checkin", formatter.format(this.checkin))
//                .write("checkout", formatter.format(this.checkout))
//                .writeEnd()
//                .close();
//        return sw.toString();
    }
}

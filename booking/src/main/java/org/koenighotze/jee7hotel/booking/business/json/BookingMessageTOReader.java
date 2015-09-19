package org.koenighotze.jee7hotel.booking.business.json;

import org.koenighotze.jee7hotel.booking.business.events.BookingMessageTO;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author koenighotze
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class BookingMessageTOReader implements MessageBodyReader<BookingMessageTO> {
    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return BookingMessageTO.class.isAssignableFrom(aClass);
    }

    @Override
    public BookingMessageTO readFrom(Class<BookingMessageTO> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> multivaluedMap, InputStream inputStream) throws IOException, WebApplicationException {
        JsonReader reader = Json.createReader(inputStream);

        JsonObject jsonObject = reader.readObject();
        Long guestId = jsonObject.getJsonNumber("guestId").longValue();
        String roomNumber = jsonObject.getString("roomNumber");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate checkin = LocalDate.parse(jsonObject.getString("checkin"), formatter);
        LocalDate checkout = LocalDate.parse(jsonObject.getString("checkout"), formatter);
        return new BookingMessageTO(guestId, roomNumber, checkin, checkout);
    }
}

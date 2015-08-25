package org.koenighotze.jee7hotel.booking.business.json;

import org.koenighotze.jee7hotel.booking.business.events.BookingMessageTO;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;

/**
 * @author koenighotze
 */
public class BookingMessageTOWriter implements MessageBodyWriter<BookingMessageTO> {
    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return BookingMessageTO.class.isAssignableFrom(aClass);
    }

    @Override
    public long getSize(BookingMessageTO bookingMessageTO, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(BookingMessageTO bookingMessageTO, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> multivaluedMap, OutputStream outputStream) throws IOException, WebApplicationException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        JsonGenerator writer = Json.createGenerator(outputStream);
        writer.writeStartObject()
                .write("guestId", bookingMessageTO.getGuest())
                .write("roomNumber", bookingMessageTO.getRoom())
                .write("checkin", formatter.format(bookingMessageTO.getCheckin()))
                .write("checkout", formatter.format(bookingMessageTO.getCheckout()))
                .writeEnd();
        writer.flush();
    }
}

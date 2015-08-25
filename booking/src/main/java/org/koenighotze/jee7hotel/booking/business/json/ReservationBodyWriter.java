package org.koenighotze.jee7hotel.booking.business.json;

import org.koenighotze.jee7hotel.domain.Reservation;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @author koenighotze
 */

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class ReservationBodyWriter  implements MessageBodyWriter<Reservation> {
    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return Reservation.class.isAssignableFrom(aClass);
    }

    @Override
    public long getSize(Reservation reservation, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(Reservation reservation, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> multivaluedMap, OutputStream outputStream) throws IOException, WebApplicationException {
        JsonGenerator writer = Json.createGenerator(outputStream);
        writer.writeStartObject()
                .write("Foo", "Bar")
                .writeEnd();
        writer.flush();
    }
}

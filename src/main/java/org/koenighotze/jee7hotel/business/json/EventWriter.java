package org.koenighotze.jee7hotel.business.json;

import org.koenighotze.jee7hotel.business.eventsource.Event;

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

/**
 * Created by dschmitz on 27.11.14.
 */
public class EventWriter implements MessageBodyWriter<Event> {
    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return Event.class.isAssignableFrom(aClass);
    }

    @Override
    public long getSize(Event event, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(Event event, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> multivaluedMap, OutputStream outputStream) throws IOException, WebApplicationException {
        JsonGenerator writer = Json.createGenerator(outputStream);
        writer.writeStartObject()
                .write("data", event.toJson())
                .writeEnd();
        writer.flush();


    }
}

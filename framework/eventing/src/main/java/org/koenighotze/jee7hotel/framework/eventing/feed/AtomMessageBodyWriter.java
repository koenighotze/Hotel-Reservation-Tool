package org.koenighotze.jee7hotel.framework.eventing.feed;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.writer.Writer;

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
 * @author dschmitz
 */
@Provider
@Produces(MediaType.APPLICATION_ATOM_XML)
public class AtomMessageBodyWriter implements MessageBodyWriter<Object> {
    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return (Feed.class.isAssignableFrom(type) || Entry.class.isAssignableFrom(type));
    }

    @Override
    public long getSize(Object o, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(Object feedOrEntry, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        Writer writer = new Abdera().getWriterFactory().getWriter("PrettyXML");
        writer.writeTo((feedOrEntry instanceof Feed) ? (Feed) feedOrEntry : (Entry) feedOrEntry, entityStream);
    }
}

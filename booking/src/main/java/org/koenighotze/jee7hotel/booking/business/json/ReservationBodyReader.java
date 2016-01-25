package org.koenighotze.jee7hotel.booking.business.json;

import org.koenighotze.jee7hotel.booking.business.ReservationCostCalculator;
import org.koenighotze.jee7hotel.booking.domain.Reservation;

import javax.inject.Inject;
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

import static java.time.LocalDate.parse;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Objects.requireNonNull;

/**
 * JSON reader for reservations.
 *
 * Note, that the services need to take care of values that should not be set from outside, e.g. costs should
 * obviously be recalculated.
 *
 * @author dschmitz
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class ReservationBodyReader implements MessageBodyReader<Reservation> {

    private ReservationCostCalculator reservationCostCalculator;

    public ReservationBodyReader() {
    }

    @Inject
    public ReservationBodyReader(ReservationCostCalculator reservationCostCalculator) {
        this.reservationCostCalculator = reservationCostCalculator;
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return Reservation.class.isAssignableFrom(requireNonNull(type));
    }

    @Override
    public Reservation readFrom(Class<Reservation> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        JsonReader reader = Json.createReader(entityStream);

        JsonObject jsonObject = reader.readObject();

        DateTimeFormatter formatter = ofPattern("yyyy-MM-dd");
        LocalDate checkin = parse(jsonObject.getString("checkinDate"), formatter);
        LocalDate checkout = parse(jsonObject.getString("checkoutDate"), formatter);
        return new Reservation(jsonObject.getString("guest"),
                jsonObject.getString("reservationNumber", ""),
                jsonObject.getString("assignedRoom"),
                checkin,
                checkout,
                jsonObject.getJsonNumber("costsInEuro").bigDecimalValue());
    }
}

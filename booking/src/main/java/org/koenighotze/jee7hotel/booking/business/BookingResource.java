package org.koenighotze.jee7hotel.booking.business;

import org.koenighotze.jee7hotel.booking.domain.Reservation;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * @author dschmitz
 */
@Named
@Path("bookings")
public class BookingResource {
    private static final Logger LOGGER = Logger.getLogger(BookingResource.class.getName());
    private BookingService bookingService;

    @Context
    private ResourceContext resourceContext;

    public BookingResource() {
    }

    @Inject
    public BookingResource(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public Response allReservations() {
        List<Reservation> allReservations = bookingService.getAllReservations();
        return Response.ok(allReservations).build();
    }

    @GET
    @Produces({"application/xml", "application/json"})
    @Path("/{publicId}")
    public Response reservation(@PathParam("publicId") String id) {
        Optional<Reservation> reservation = bookingService.findReservationByNumber(id);

        if (reservation.isPresent()) {
            LOGGER.info(() -> "Returning reservation " + reservation.get());
            return Response.ok(reservation.get()).build();
        }

        return Response.ok().status(NOT_FOUND).build();
    }

    @POST
    @Consumes({APPLICATION_XML, APPLICATION_JSON})
    public Response reservation(Reservation booking, @Context UriInfo uriInfo) {
        Reservation reservation = bookingService.bookRoom(booking.getGuest(), booking.getAssignedRoom(), booking.getCheckinDate(), booking.getCheckoutDate());

        URI build = uriInfo.getAbsolutePathBuilder().path(reservation.getReservationNumber()).build();
        LOGGER.info(() -> "New reservation at " + build);
        return Response.created(build).build();
    }
}

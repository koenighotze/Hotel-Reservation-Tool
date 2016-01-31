package org.koenighotze.jee7hotel.guest.business;

import org.koenighotze.jee7hotel.guest.domain.Guest;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Optional;
import java.util.logging.Logger;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.*;

/**
 * @author dschmitz
 */
@Named
@Path("/guests")
public class GuestResource {
    private static final Logger LOGGER = Logger.getLogger(GuestResource.class.getName());

    @Context
    private ResourceContext resourceContext;

    private GuestService guestService;

    public GuestResource() {
    }

    @Inject
    public GuestResource(GuestService guestService) {
        this.guestService = guestService;
    }

    @POST
    @Consumes({APPLICATION_XML, APPLICATION_JSON})
    public Response guest(Guest guest, @Context UriInfo uriInfo) {
        guestService.saveGuest(guest);

        URI build = uriInfo.getAbsolutePathBuilder().path(guest.getPublicId()).build();

        LOGGER.info(() -> "New guest at " + build.toASCIIString());
        return created(build).build();
    }

    @DELETE
    @Consumes({APPLICATION_XML, APPLICATION_JSON})
    @Path("/{guestId}")
    public Response deleteGuest(@PathParam("guestId") String publicId) {
        if (guestService.deleteGuest(publicId)) {
            return ok(publicId).build();

        }
        return status(NOT_FOUND).build();
    }

    @GET
    @Produces({APPLICATION_XML, APPLICATION_JSON})
    @Path("/{guestId}")
    public Response guestByPublicId(@PathParam("guestId") String publicId) {
        Optional<Guest> guestOptional = guestService.findByPublicId(publicId);

        if (guestOptional.isPresent()) {
            return ok(guestOptional.get()).build();
        }

        return status(NOT_FOUND).build();
    }

    @GET
    @Produces({APPLICATION_XML, APPLICATION_JSON})
    public Response allGuests() {
        return ok(guestService.getAllGuests()).build();
    }
}

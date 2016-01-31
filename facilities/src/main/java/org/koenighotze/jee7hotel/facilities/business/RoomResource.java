package org.koenighotze.jee7hotel.facilities.business;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

import static java.util.Optional.of;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.status;

/**
 * @author dschmitz
 */
@Named
@Path("/rooms")
public class RoomResource {
    private static final Logger LOGGER = Logger.getLogger(RoomResource.class.getName());

    @Context
    private ResourceContext resourceContext;
    private RoomService roomService;

    public RoomResource() {
    }

    @Inject
    public RoomResource(RoomService roomService) {
        this.roomService = roomService;
    }

    @GET
    @Path("/{publicId}")
    @Produces(APPLICATION_JSON)
    public Response getRoom(@PathParam("publicId") String publicId) {
        return roomService
                .findRoomByNumber(publicId)
                .<Response>flatMap(room ->
                        of(ok(room).build()))
                .orElse(status(NOT_FOUND).build());
    }

    @GET
    @Produces(APPLICATION_JSON)
    public Response getRooms() {
        return ok().build();
    }
}

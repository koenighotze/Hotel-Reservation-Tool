package org.jug.view;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by shekhargulati on 21/03/14.
 */
@Provider
public class ViewExceptionMapper implements ExceptionMapper<ViewException> {

    @Override
    public Response toResponse(ViewException exception) {
        View view = View.of("/500", exception.getTemplateEngine()).withModel("error",exception.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(view).build();
    }
}

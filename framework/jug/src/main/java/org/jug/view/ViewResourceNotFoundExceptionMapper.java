package org.jug.view;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by shekhargulati on 21/03/14.
 */
@Provider
public class ViewResourceNotFoundExceptionMapper implements ExceptionMapper<ViewResourceNotFoundException> {

    @Override
    public Response toResponse(ViewResourceNotFoundException exception) {
        View view = View.of("/404", exception.getTemplateEngine()).withModel("error",exception.getMessage());
        return Response.status(Response.Status.NOT_FOUND).entity(view).build();
    }
}

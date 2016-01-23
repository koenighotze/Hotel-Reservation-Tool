package org.jug.filters;

import org.jug.view.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by shekhargulati on 18/04/14.
 */
@Provider
@LoggedIn
public class AuthenticationFilter implements ContainerRequestFilter {

    @Context
    private HttpServletRequest request;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        logger.info("In AuthenticationFilter ...");
        HttpSession session = request.getSession(false);
        logger.info("Session " + session);
        if (session != null) {
            final String principal = session.getAttribute("principal") != null ? session.getAttribute("principal")
                    .toString() : null;
            String sessionId = session.getId();
            logger.info("Session principal " + principal);
            logger.info("Sesssion id" + sessionId);
            if (principal != null) {
                requestContext.setSecurityContext(new SecurityContext() {

                    @Override
                    public boolean isUserInRole(String role) {
                        return false;
                    }

                    @Override
                    public boolean isSecure() {
                        return true;
                    }

                    @Override
                    public Principal getUserPrincipal() {
                        return new Principal() {

                            @Override
                            public String getName() {
                                return principal;
                            }
                        };
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return null;
                    }
                });
            }
        }
        if (session == null || session.getAttribute("principal") == null) {
            logger.info("Returing Forbidden...");
            MediaType mediaType = requestContext.getMediaType();
            logger.info("Media Type : " + mediaType);
            if (mediaType != null && mediaType.toString().equals(MediaType.APPLICATION_JSON)) {
                Map<String, String> json = new HashMap<>();
                json.put("msg", "You are not logged in");
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).entity(json).build());
                return;
            }
            requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).entity(View.of("/signin", true))
                    .build());
        }
    }
}
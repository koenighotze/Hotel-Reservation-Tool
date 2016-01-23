package org.jug.filters;


import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

/**
 * Created by shekhargulati on 18/04/14.
 */
@Provider
@EnableSession
public class EnableSessionFilter implements ContainerRequestFilter {

    @Context
    private HttpServletRequest request;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        HttpSession existingSession = request.getSession(false);
        logger.info("Existing Session : " + existingSession);
        if (existingSession == null) {
            HttpSession newSession = request.getSession(true);
            newSession.setMaxInactiveInterval(-1);
            logger.info("Created new session with id " + newSession.getId());
        }
    }
}
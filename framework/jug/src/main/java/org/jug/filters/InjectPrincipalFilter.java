package org.jug.filters;

import java.io.IOException;
import java.security.Principal;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

@Provider
@InjectPrincipal
public class InjectPrincipalFilter implements ContainerRequestFilter {

    @Context
    private HttpServletRequest request;
    private Logger logger = Logger.getLogger(getClass().getName());

    public void filter(ContainerRequestContext requestContext) throws IOException {
        this.logger.info("Inside InjectPrincipalFilter ...");
        HttpSession session = this.request.getSession(false);
        this.logger.info("Session " + session);
        if (session != null) {
            final String principal = session.getAttribute("principal") != null ? session.getAttribute("principal").toString() : null;

            String sessionId = session.getId();
            this.logger.info("Session principal " + principal);
            this.logger.info("Sesssion id" + sessionId);
            if (principal != null)
                requestContext.setSecurityContext(new SecurityContext() {
                    public boolean isUserInRole(String role) {
                        return false;
                    }

                    public boolean isSecure() {
                        return true;
                    }

                    public Principal getUserPrincipal() {
                        return new Principal() {
                            public String getName() {
                                return principal;
                            }
                        };
                    }

                    public String getAuthenticationScheme() {
                        return null;
                    }
                });
        }
    }
}
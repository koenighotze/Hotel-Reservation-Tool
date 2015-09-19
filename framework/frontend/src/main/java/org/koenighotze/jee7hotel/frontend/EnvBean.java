package org.koenighotze.jee7hotel.frontend;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 * @author koenighotze
 */
@Named
@ApplicationScoped
public class EnvBean {
    public String getWebSocketPath() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        String port = System.getenv("OPENSHIFT_INTERNAL_PORT");
        if (null == port) {
            port = "" + request.getServerPort();
        }

        return String.format("ws://%s:%s%s", request.getServerName(), port, request.getContextPath());
    }
}

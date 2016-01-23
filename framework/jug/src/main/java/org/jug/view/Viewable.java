package org.jug.view;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import java.io.IOException;

public interface Viewable {

    public String render(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, WebApplicationException;

    public boolean isRedirect();

    public String getPath();

    public boolean isAbsolute();
}

package org.koenighotze.jee7hotel.frontend;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by dschmitz on 11.12.14.
 */
@WebFilter()
public class HeaderFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(HeaderFilter.class.getName());
    private boolean developmentMode;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String contextPath = filterConfig.getServletContext().getContextPath();
        LOGGER.info(() -> "Init on filter " + this + " on path " + contextPath);

        this.developmentMode = true;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String serverName = req.getServerName();
        int port = req.getServerPort();
        String protocol = "http://";
        String localhost = protocol + serverName + ":" + port;

        HttpServletResponse res = (HttpServletResponse) response;
        String cps = "script-src 'unsafe-inline' https://ajax.googleapis.com " + localhost;
        res.setHeader("Content-Security-Policy", cps);

        LOGGER.fine(() -> "Added CPS Header " + cps);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}

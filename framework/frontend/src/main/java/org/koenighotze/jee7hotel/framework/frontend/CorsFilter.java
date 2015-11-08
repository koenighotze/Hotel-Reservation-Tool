package org.koenighotze.jee7hotel.framework.frontend;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author dschmitz
 */
@WebFilter(filterName = "CorsFilter",
        urlPatterns = {"/*"})
public class CorsFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Thread.dumpStack();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        System.err.println("Filtering...");

        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("X-Blafasel", "HULLEWU");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        chain.doFilter(request, res);
    }

    @Override
    public void destroy() {

    }
}

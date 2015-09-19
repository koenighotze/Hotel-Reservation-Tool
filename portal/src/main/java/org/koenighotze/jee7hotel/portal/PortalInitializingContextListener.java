package org.koenighotze.jee7hotel.portal;

import org.jug.JugFilterDispatcher;

import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.EnumSet;

import static javax.servlet.DispatcherType.REQUEST;

/**
 * @author dschmitz
 */
@WebListener
public class PortalInitializingContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        Dynamic filter = ctx.addFilter("JUGFilter", JugFilterDispatcher.class);
        filter.setInitParameter("javax.ws.rs.Application", ApplicationConfig.class.getName());
        filter.addMappingForUrlPatterns(EnumSet.of(REQUEST), true, "/*");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}

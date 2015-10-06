

package org.koenighotze.jee7hotel.framework.application.monitor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.logging.Logger;

import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

/**
 * @author dschmitz
 */
@Startup
@Singleton
@Path("monitor")
public class ApplicationMonitorBean {
    private static final Logger LOGGER = Logger.getLogger(ApplicationMonitorBean.class.getName());

    // TODO add Web Socket that publishes state of the union

    @PostConstruct
    public void startup() {
        LOGGER.info("Application is running at " + ISO_DATE_TIME.format(now()));
    }

    @Produces({"application/xml", "application/json"})
    public ApplicationStatus getApplicationStatus() {
        // TODO: obviously needs some work
        return new ApplicationStatus();
    }

    @Schedule(persistent = false, minute = "*/1", hour = "*", second = "0")
    public void watchdog() {
        LOGGER.info("Application still running");
    }

    @PreDestroy
    public void shuttingDown() {
        LOGGER.info("Application is shutting down at " + ISO_DATE_TIME.format(now()));
    }
}

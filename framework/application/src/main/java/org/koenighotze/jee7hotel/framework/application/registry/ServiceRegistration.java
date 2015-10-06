package org.koenighotze.jee7hotel.framework.application.registry;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

/**
 * Naive implementation of a service registry.
 *
 * @author dschmitz
 */
@Named
@ApplicationScoped
@SuppressWarnings("unused")
public class ServiceRegistration {
    @Produces
    @AddReservationUrl
    public String getAddNewReservationUrl() {
        // this is obviously bad...but ok for our example
        // actually this should return null, if the URL is unavailable
        // TODO: check via GET, if url is available
        return "http://localhost:8080/booking/add_new_reservation.html";
    }
}

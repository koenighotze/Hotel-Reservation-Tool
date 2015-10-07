package org.koenighotze.jee7hotel.framework.application.registry;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.Logger;

import static java.time.LocalDateTime.now;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.config.SocketConfig.DEFAULT;
import static org.apache.http.config.SocketConfig.copy;

/**
 * Naive implementation of a service registry.
 *
 * @author dschmitz
 */
@Named
@ApplicationScoped
@SuppressWarnings("unused")
public class ServiceRegistration {

    private static final Logger LOGGER = Logger.getLogger(ServiceRegistration.class.getName());

    private static final String ADD_NEW_RESERVATION_URL = "http://localhost:8080/booking/add_new_reservation.html";
    private static final String HOST_URL = "http://localhost:8080";

    private final long MINUTES_PER_CHECK = 1L;
    private boolean available = false;
    private LocalDateTime lastCheck = now().minusDays(1L);

    @Produces
    @AddReservationUrl
    public String getAddNewReservationUrl() {
        if (lastCheck.isBefore(now().minusMinutes(MINUTES_PER_CHECK))) {
            available = false;

            // this is obviously bad...but ok for our example
            LOGGER.info(() -> "Checking availability of " + ADD_NEW_RESERVATION_URL);
            SocketConfig socketConfig = copy(DEFAULT).setSoTimeout(2000).build();
            try (CloseableHttpClient client = HttpClientBuilder.create().setDefaultSocketConfig(socketConfig).build(); CloseableHttpResponse response = client.execute(HttpHost.create(HOST_URL), new HttpGet(ADD_NEW_RESERVATION_URL))) {
                available = SC_OK == response.getStatusLine().getStatusCode();

                if (SC_OK != response.getStatusLine().getStatusCode()) {
                    LOGGER.log(WARNING, () -> "Cannot access url for new reservations!");
                }
            } catch (IOException e) {
                LOGGER.log(SEVERE, e, () -> "Cannot access url for new reservations!");
            }
        }

        return available ? ADD_NEW_RESERVATION_URL : null;
    }
}

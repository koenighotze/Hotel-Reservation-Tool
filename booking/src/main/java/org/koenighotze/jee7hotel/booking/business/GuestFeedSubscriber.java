package org.koenighotze.jee7hotel.booking.business;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Feed;
import org.apache.abdera.parser.Parser;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Logger;

/**
 * @author dschmitz
 */
@Startup
@Singleton
public class GuestFeedSubscriber {
    private static final Logger LOGGER = Logger.getLogger(GuestFeedSubscriber.class.getName());

    private static final String GUEST_URL = "http://localhost:8080/guest/rest/guest";

    private Abdera abdera;

    @PostConstruct
    public void init() {
        LOGGER.info(() -> "Initialized feeder...");
        abdera = new Abdera();
    }

    @Schedule(minute = "*/1", hour = "*")
    public void updateFeed() {
        LOGGER.info(() -> "Reading feed from " + GUEST_URL);

        Parser parser = abdera.getParser();

        // TODO: use http client to set timeout
        try {
            URL url = new URL(GUEST_URL);

            Document<Feed> doc = parser.parse(url.openStream(), url.toString());
            Feed feed = doc.getRoot();
            feed.getEntries().stream().forEach(entry -> {
                LOGGER.info(() -> "Read Id " + entry.getId());
                LOGGER.info(() -> "Read link " + entry.getLinks());
                entry.getLinks().stream().forEach(link -> {
                    try {
                        fetchGuest(link.getHref().toURI());
                    } catch (URISyntaxException e) {
                        LOGGER.warning(() -> "Cannot load guest details for " + link.getHref());
                    }
                });
            });
        } catch (IOException e) {
            LOGGER.warning(() -> "Cannot load guest data from " + GUEST_URL + "! " + e.getMessage());
        }
    }

    private void fetchGuest(URI link) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(link);
        JsonObject response = target.request(MediaType.APPLICATION_JSON).get(JsonObject.class);

        LOGGER.info(() -> "Read guest " + response.toString());
    }
}

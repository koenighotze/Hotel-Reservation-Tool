package org.koenighotze.jee7hotel.booking.business;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Feed;
import org.apache.abdera.parser.Parser;
import org.koenighotze.jee7hotel.booking.domain.GuestModel;
import org.koenighotze.jee7hotel.booking.persistence.GuestModelRepository;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.logging.Logger;

import static java.lang.String.format;
import static java.lang.System.getenv;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;
import static javax.ws.rs.client.ClientBuilder.newClient;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.apache.commons.lang3.StringUtils.defaultString;

/**
 * @author dschmitz
 */
@Startup
@Singleton
public class GuestFeedSubscriber {
    static final String GUEST_URI_ENV_PROPERTY_KEY = "GUEST_URI";
    private static final Logger LOGGER = Logger.getLogger(GuestFeedSubscriber.class.getName());

    private Date lastUpdate = null;

    @Inject
    private Abdera abdera;

    @Inject
    private GuestModelRepository guestModelRepository;

    @PostConstruct
    public void init() {
        updateFeed();
    }

    String buildGuestUrl() {
        return format("http://%s/guest/rest/guest", determineGuestHostAndPort());
    }

    // TODO: inject me
    String determineGuestHostAndPort() {
        return defaultString(getenv(GUEST_URI_ENV_PROPERTY_KEY), "localhost:8080");
    }

    @Schedule(minute = "*/1", hour = "*")
    public void updateFeed() {

        final String guestUrl = buildGuestUrl();
        LOGGER.info(() -> "Reading feed from " + guestUrl);

        Parser parser = abdera.getParser();

        // TODO: use http client to set timeout
        try {
            URL url = new URL(guestUrl);
            Document<Feed> doc = parser.parse(url.openStream(), url.toString());
            Feed feed = doc.getRoot();
            Date updated = feed.getUpdated();
            if (null == lastUpdate || lastUpdate.before(updated)) {
                lastUpdate = updated;
                LOGGER.info(() -> "Feed contains unknown data...continue to parse it");
            } else {
                LOGGER.info(() -> "Feed contains stale data...skip parsing entries");
                return;
            }

            feed.getEntries().stream().forEach(entry -> {
                LOGGER.info(() -> "Read Id " + entry.getId());
                entry.getLinks().stream().forEach(link -> {
                    try {
                        LOGGER.info(() -> "Read link " + link.getHref());
                        fetchGuest(link.getHref().toURI());
                    } catch (Exception e) {
                        LOGGER.log(WARNING, e, () -> "Cannot load guest details for " + link.getHref());
                    }
                });
            });
        } catch (Exception e) { // must not fail...ev0r
            LOGGER.log(SEVERE, e, () -> "Cannot load guest data from " + guestUrl + "! " + e.getMessage());
        }
    }

    private void fetchGuest(URI link) {
        Client client = newClient();
        WebTarget target = client.target(link);
        JsonObject response = target.request(APPLICATION_JSON).get(JsonObject.class);

        LOGGER.fine(() -> "Read guest " + response.toString());

        GuestModel model = new GuestModel(response.getString("publicId"), response.getString("name"));
        if (guestModelRepository.findByPublicId(model.getPublicId()).isPresent()) {
            LOGGER.fine(() -> "Guest " + model + " is already known...will not import");
        } else {
            guestModelRepository.storeGuestModel(model);
        }
    }
}

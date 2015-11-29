package org.koenighotze.jee7hotel.guest.business;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.koenighotze.jee7hotel.framework.application.logging.PerformanceLogger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.logging.Logger;

import static java.time.ZoneOffset.UTC;
import static java.util.logging.Level.WARNING;

/**
 * @author dschmitz
 */

@Named
@Stateless
@Path("guest")
@Interceptors({
        PerformanceLogger.class
})
public class GuestAtomFeed {
    private static final Logger LOGGER = Logger.getLogger(GuestAtomFeed.class.getName());

    private Abdera abdera;
    private GuestService guestService;

    public GuestAtomFeed() {
    }

    @Inject
    public GuestAtomFeed(GuestService guestService, Abdera abdera) {
        this.guestService = guestService;
        this.abdera = abdera;
    }

    private String getLocalhostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            LOGGER.log(WARNING, e, () -> "Cannot determine localhost name");
            return "localhost";
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public Feed getGuestFeed(@Context UriInfo uriInfo) {
        Feed feed = abdera.newFeed();

        feed.setId("tag:koenighotze.org,2015:/guest");
        feed.setTitle("Guests");
        feed.setUpdated(new Date());

        final String baseUri = uriInfo.getBaseUri().toString();
        //feed.addLink("http://" + getLocalhostName() + ":8080/guest/rest/feed", "self"); // todo self ref
        feed.addLink(baseUri + "feed", "self"); // todo self ref
        guestService.getAllGuests().stream().forEach(guest -> {
            Entry entry = feed.addEntry();
            entry.setId(guest.getId() + "");
            entry.setUpdated(Date.from(guest.getLastUpdate().toInstant(UTC)));
            entry.setAttributeValue("Version", guest.getVersion() + "");
//            entry.setTitle("Entry title");
//            entry.setSummaryAsHtml("<p>This is the entry title</p>");
//            entry.setPublished(new Date());
//            entry.setContent(guest.toString());
//            entry.addLink("http://localhost:8080/guest/rest/guest/" + guest.getId());
            entry.addLink(baseUri + "guest/" + guest.getId());
        });

        return feed;
    }
}

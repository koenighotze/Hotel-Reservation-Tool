package org.koenighotze.jee7hotel.guest.business;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.parser.stax.FOMFeed;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.jee7hotel.guest.domain.Guest;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.UriInfo;
import java.util.List;

import static java.net.URI.create;
import static java.time.LocalDateTime.now;
import static java.util.Collections.singletonList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author David Schmitz
 */
@RunWith(MockitoJUnitRunner.class)
public class GuestAtomFeedTest {

    @Mock
    private GuestService guestService;

    @Mock
    private Abdera abdera;

    @Mock
    private UriInfo uriInfo;

    @Test
    public void the_feed_exposes_the_publicid() {
        Guest guest = new Guest("123", "foo", "foo@bar.de");
        guest.setLastUpdate(now());
        when(abdera.newFeed()).thenReturn(new FOMFeed());
        when(uriInfo.getBaseUri()).thenReturn(create("http://localhost/"));
        when(guestService.getAllGuests()).thenReturn(singletonList(guest));

        GuestAtomFeed guestAtomFeed = new GuestAtomFeed(guestService, abdera);
        Feed guestFeed = guestAtomFeed.getGuestFeed(uriInfo);
        List<Entry> entries = guestFeed.getEntries();

        assertThat(entries).isNotEmpty();
        Entry entry = entries.get(0);
        assertThat(entry.getLinks().get(0).getHref().toASCIIString()).endsWith("/guests/123");
    }
}
package org.koenighotze.jee7hotel.booking.business;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static java.lang.String.format;
import static java.lang.System.getenv;
import static org.fest.assertions.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assume.assumeThat;
import static org.koenighotze.jee7hotel.booking.business.GuestFeedSubscriber.GUEST_URI_ENV_PROPERTY_KEY;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author dschmitz
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({GuestFeedSubscriber.class})
public class GuestFeedSubscriberTest {
    @Test
    public void if_the_environment_variable_is_unset_the_guest_url_defaults_to_localhost() {
        assumeThat(getenv(GUEST_URI_ENV_PROPERTY_KEY), is(nullValue()));

        assertThat(new GuestFeedSubscriber().determineGuestHostAndPort()).isEqualTo("localhost:8080");
    }

    @Test
    public void if_the_environment_variable_is_set_the_guest_url_is_configured() {
        mockStatic(System.class);
        final String expectedConfig = "HOST:PORT";
        when(System.getenv(GUEST_URI_ENV_PROPERTY_KEY)).thenReturn(expectedConfig);

        assertThat(new GuestFeedSubscriber().determineGuestHostAndPort()).isEqualTo(expectedConfig);
    }

    @Test
    public void the_guest_url_is_set_to_localhost_per_default() {
        assumeThat(getenv(GUEST_URI_ENV_PROPERTY_KEY), is(nullValue()));

        assertThat(new GuestFeedSubscriber().buildGuestUrl()).startsWith("http://localhost:8080/guest");
    }

    @Test
    public void the_guest_url_includes_the_environment_config() {
        mockStatic(System.class);
        final String expectedConfig = "HOST:PORT";
        when(System.getenv(GUEST_URI_ENV_PROPERTY_KEY)).thenReturn(expectedConfig);
        assertThat(new GuestFeedSubscriber().buildGuestUrl()).startsWith(format("http://%s/guest", expectedConfig));
    }
}
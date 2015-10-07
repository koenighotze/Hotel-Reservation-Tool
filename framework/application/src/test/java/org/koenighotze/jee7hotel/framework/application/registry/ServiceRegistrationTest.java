package org.koenighotze.jee7hotel.framework.application.registry;

import mockit.Expectations;
import mockit.Mocked;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

import java.io.IOException;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.fest.assertions.Assertions.assertThat;

/**
 * @author dschmitz
 */
public class ServiceRegistrationTest {

    @Mocked
    private CloseableHttpResponse response;

    @Mocked
    private CloseableHttpClient client;

    @Mocked
    private HttpClientBuilder builder;

    @Test
    public void the_reservation_url_is_returned_if_available() throws IOException {
        prepareExpectations(SC_OK);

        assertThat(new ServiceRegistration().getAddNewReservationUrl()).isNotEmpty();
    }

    @Test
    public void null_is_returned_if_the_service_is_not_found() throws IOException {
        prepareExpectations(SC_NOT_FOUND);

        ServiceRegistration serviceRegistration = new ServiceRegistration();

        assertThat(serviceRegistration.getAddNewReservationUrl()).isNull();
    }

    protected void prepareExpectations(final int httpStatusCode) throws IOException {
        new Expectations() {
            {
                builder.build();
                result = client;
            }

            {
                response.getStatusLine().getStatusCode();
                result = httpStatusCode;
            }

            {
                client.execute(withInstanceOf(HttpHost.class), withInstanceOf(HttpRequest.class));
                result = response;
            }
        };
    }
}
package org.koenighotze.jee7hotel.framework.integration;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.HttpHeaders.LOCATION;
import static javax.ws.rs.core.Response.Status.*;
import static org.fest.assertions.Assertions.assertThat;

/**
 * Support class for REST tests.
 *
 * @author dschmitz
 */
public class RestAssertions {

    public static void assertCreated(Response response) {
        assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());
        final String expectedPublicId = response.getHeaderString(LOCATION);
        assertThat(expectedPublicId).isNotEmpty();
    }

    public static void assertNotFound(Response response) {
        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
    }

    public static void assertOk(Response response) {
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
    }
}

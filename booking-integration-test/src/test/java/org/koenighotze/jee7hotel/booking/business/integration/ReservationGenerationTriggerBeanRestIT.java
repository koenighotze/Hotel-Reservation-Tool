package org.koenighotze.jee7hotel.booking.business.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.jee7hotel.booking.business.events.BookingMessageTO;
import org.koenighotze.jee7hotel.booking.business.json.BookingMessageTOWriter;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.net.URL;
import java.time.LocalDate;
import java.util.logging.Logger;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.koenighotze.jee7hotel.framework.integration.BaseArquillianSetup.createStandardDeployment;

@RunWith(Arquillian.class)
public class ReservationGenerationTriggerBeanRestIT {
    private static final Logger LOGGER = Logger.getLogger(ReservationGenerationTriggerBeanRestIT.class.getName());

    @Deployment
    public static WebArchive createMicroDeployment() {
        return createStandardDeployment(ReservationGenerationBeanArquillianIT.class.getPackage());
    }

    @Test
    @RunAsClient
    public void testRestInterface(@ArquillianResource URL baseURI) throws InterruptedException {
        String targetUrl = baseURI + "rest/bookingstrigger";

        // Client does not implement autoclosable :(
        Client client = null;
        WebTarget webTarget = null;

        try {
            client = ClientBuilder.newClient();
            webTarget = client.target(targetUrl);
            BookingMessageTO messageTO = new BookingMessageTO(12L, "abc", LocalDate.now(), LocalDate.now().plusDays(1));

            Response response = webTarget
                    .register(BookingMessageTOWriter.class)
                    .register(BookingMessageTO.class)
                    .request()
                    .post(Entity.entity(messageTO, APPLICATION_JSON));

            assertThat(response.getStatusInfo().getFamily(), is(sameInstance(Response.Status.Family.SUCCESSFUL)));
        } finally {
            if (null != client) {
                client.close();
            }
        }
    }
}
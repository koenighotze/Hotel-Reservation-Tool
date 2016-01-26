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

import static java.lang.Thread.sleep;
import static java.time.LocalDate.now;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.koenighotze.jee7hotel.framework.integration.BaseArquillianSetup.createStandardDeployment;

@RunWith(Arquillian.class)
public class ReservationGenerationTriggerBeanRestIT {

    @Deployment
    public static WebArchive createMicroDeployment() {
        return createStandardDeployment(ReservationGenerationBeanIT.class.getPackage());
    }

    @Test
    @RunAsClient
    public void testRestInterface(@ArquillianResource URL baseURI) throws InterruptedException {
        String targetUrl = baseURI + "rest/trigger";

        // Client does not implement autoclosable :(
        Client client = null;

        try {
            client = ClientBuilder.newClient();
            WebTarget webTarget = client.target(targetUrl);
            BookingMessageTO messageTO = new BookingMessageTO(12L, "abc", now(), now().plusDays(1));

            Response response = webTarget
                    .register(BookingMessageTOWriter.class)
                    .register(BookingMessageTO.class)
                    .request()
                    .post(Entity.entity(messageTO, APPLICATION_JSON));

            assertThat(response.getStatusInfo().getFamily(), is(sameInstance(SUCCESSFUL)));

            // now give the app some time to process the message
            sleep(11000L);
        } finally {
            if (null != client) {
                client.close();
            }
        }
    }
}
package org.koenighotze.jee7hotel.booking.business.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.jee7hotel.booking.business.events.BookingMessageTO;
import org.koenighotze.jee7hotel.booking.business.json.BookingMessageTOWriter;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.formatter.Formatters.VERBOSE;
import static org.junit.Assert.assertThat;
import static org.koenighotze.jee7hotel.booking.business.integration.BaseArquillianSetup.buildMavenLibraryDependencies;
import static org.koenighotze.jee7hotel.booking.business.integration.BaseArquillianSetup.readJBossWebXml;

@RunWith(Arquillian.class)
public class ReservationGenerationTriggerBeanRestIT {
    private static final Logger LOGGER = Logger.getLogger(ReservationGenerationTriggerBeanRestIT.class.getName());

    @Deployment
    public static Archive<?> createMicroDeployment() {
        try {
            String jbossWebXml = readJBossWebXml(ReservationGenerationBeanArquillianIT.class.getPackage());

            File[] libs = buildMavenLibraryDependencies();

            WebArchive baseDeployment = create(WebArchive.class)
                    .addAsWebInfResource(jbossWebXml, "jboss-web.xml")
                    .addAsLibraries(libs);

            LOGGER.info(() -> baseDeployment.toString(VERBOSE));
            return baseDeployment;
        } catch (Exception e) {
            LOGGER.log(SEVERE, e, () -> "Creating deployment failed");
            throw e;
        }
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
                    .post(Entity.entity(messageTO, MediaType.APPLICATION_JSON));

            assertThat(response.getStatusInfo().getFamily(), is(sameInstance(Response.Status.Family.SUCCESSFUL)));
        }
        finally {
            client.close();
        }
    }
}
package org.koenighotze.jee7hotel.guest.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.base.asset.AssetUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.jee7hotel.guest.frontend.Message;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.net.URL;

import static org.fest.assertions.Assertions.assertThat;
import static org.koenighotze.jee7hotel.framework.integration.BaseArquillianSetup.createStandardDeployment;

/**
 * @author dschmitz
 */
@RunWith(Arquillian.class)
public class MessagesArquillianRestIT {
    private Client client;

    @Deployment
    public static Archive<?> createMicroDeployment() {
        WebArchive deployment = createStandardDeployment(MessagesArquillianRestIT.class.getPackage());

        deployment.addAsWebInfResource(AssetUtil.getClassLoaderResourceName(MessagesArquillianRestIT.class.getPackage(), "faces-config.xml"), "faces-config.xml");
        deployment.addAsWebInfResource(AssetUtil.getClassLoaderResourceName(MessagesArquillianRestIT.class.getPackage(), "web.xml"), "web.xml");
        return deployment;
    }

    @Before
    public void setupWebClient() {
        client = ClientBuilder.newClient();
    }

    @After
    public void cleanupWebClient() {
        if (null != client) {
            client.close();
        }
    }

    @Test
    @RunAsClient
    public void messages_can_be_fetched_via_get(@ArquillianResource URL baseURI) throws InterruptedException {
        String targetUrl = baseURI + "rest/messages";

        WebTarget webTarget = client.target(targetUrl);

        Message[] response = webTarget
                .request().get(Message[].class);

        assertThat(response.length).isGreaterThan(0);
    }

}

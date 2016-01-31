package org.koenighotze.jee7hotel.framework.integration;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import java.net.URL;

import static javax.ws.rs.client.ClientBuilder.newClient;

/**
 * @author dschmitz
 */
@RunWith(Arquillian.class)
public abstract class AbstractBaseArquillianIT {
    @ArquillianResource
    protected URL baseURI;

    protected Client client;

    @Before
    public void setupWebClient() {
        client = newClient();
        // Connectiontimeout in milliseconds
        client.property("jersey.config.client.connectTimeout", 1000);
        // Read timeout in milliseconds
        client.property("jersey.config.client.readTimeout", 1000);
    }

    @After
    public void cleanupWebClient() {
        if (null != client) {
            client.close();
        }
    }

    protected final String getTargetUrl() {
        return baseURI + getBaseServiceUrl();
    }

    protected abstract String getBaseServiceUrl();
}

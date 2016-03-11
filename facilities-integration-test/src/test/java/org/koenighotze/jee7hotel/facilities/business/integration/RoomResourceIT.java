package org.koenighotze.jee7hotel.facilities.business.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.jee7hotel.facilities.domain.Room;
import org.koenighotze.jee7hotel.framework.integration.AbstractBaseArquillianIT;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.fest.assertions.Assertions.assertThat;
import static org.koenighotze.jee7hotel.framework.integration.BaseArquillianSetup.createStandardDeployment;
import static org.koenighotze.jee7hotel.framework.integration.RestAssertions.assertOk;

/**
 * @author dschmitz
 */
@RunWith(Arquillian.class)
public class RoomResourceIT extends AbstractBaseArquillianIT {
    @Deployment
    public static WebArchive createMicroDeployment() {
        return createStandardDeployment(RoomResourceIT.class.getPackage());
    }

    @Test
    @RunAsClient
    public void get_room() {
        Response response = client.target(getTargetUrl()).path("001").request(APPLICATION_JSON_TYPE).get();
        assertOk(response);

        Room room = response.readEntity(Room.class);
        assertThat(room).isNotNull();
        assertThat(room.getRoomNumber()).isEqualTo("001");
    }

    @Override
    protected String getBaseServiceUrl() {
        return "rest/rooms";
    }
}

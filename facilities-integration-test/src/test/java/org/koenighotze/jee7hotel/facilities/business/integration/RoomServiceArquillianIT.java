package org.koenighotze.jee7hotel.facilities.business.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.jee7hotel.facilities.business.RoomService;
import org.koenighotze.jee7hotel.framework.integration.BaseArquillianSetup;

import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * @author dschmitz
 */
@RunWith(Arquillian.class)
public class RoomServiceArquillianIT {
    private static final Logger LOGGER = Logger.getLogger(RoomServiceArquillianIT.class.getName());

    @Inject
    private RoomService roomService;

    @Deployment
    public static WebArchive createMicroDeployment() {
        return BaseArquillianSetup.createStandardDeployment(RoomServiceArquillianIT.class.getPackage());
    }

    @Test
    public void nothing() {

    }

}

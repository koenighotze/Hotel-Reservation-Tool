package org.koenighotze.jee7hotel.frontend.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import java.net.URL;

/**
 * Created by dschmitz on 03.12.14.
 */
//
//@RunWith(Arquillian.class)
public class DemoUiArquillianIT {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL deploymentUrl;

//    @Deployment(testable = false)
    public WebArchive createMicroDeployment() {
        return null;
    }





}

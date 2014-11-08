

package org.koenighotze.jee7hotel.business.integration;

import org.koenighotze.jee7hotel.business.GuestService;
import org.koenighotze.jee7hotel.domain.Guest;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.junit.Assert;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.CoreMatchers.*;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.base.asset.AssetUtil;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

/**
 *
 * @author dschmitz
 */
@RunWith(Arquillian.class)
public class GuestServiceArquillianIntegrationIT {
    
    @Inject
    private GuestService guestService;
    
    @Deployment
    public static Archive<?> createMicroDeployment() {
        // get special integration test persistence unit
        String asset = AssetUtil
                .getClassLoaderResourceName(GuestServiceArquillianIntegrationIT.class.getPackage(), 
                "integration-test-persistence.xml");        
        
        // build list of needed maven dependencies
        File[] deps = Maven.resolver().loadPomFromFile("pom.xml")
                .importRuntimeDependencies()
                .resolve()
                .withTransitivity()
                .asFile();
                        
                     
        WebArchive archiv = ShrinkWrap.create(WebArchive.class)
                // add demo and all recursive packages 
                // as of yet, we do not deploy the web layer   
                .addPackages(true, "demo.business", "demo.domain") 
                .addAsResource(asset, "META-INF/persistence.xml")
                // this is not needed for jee7
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        
        for (File f : deps) {
            archiv.addAsLibraries(f);
        }
        
        System.out.println(archiv.toString(Formatters.VERBOSE));
        return archiv;
    }
    
    @Test
    public void testSimpleScenario() {
        assertNotNull(this.guestService);
        List<Guest> guests = this.guestService.findAllGuests();
        Assert.assertThat(guests, is(not(nullValue())));        
    }                
}



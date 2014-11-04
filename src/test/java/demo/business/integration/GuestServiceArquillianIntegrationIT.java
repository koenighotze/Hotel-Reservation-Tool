

package demo.business.integration;

import demo.business.GuestService;
import demo.domain.Guest;
import java.util.List;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author dschmitz
 */
@RunWith(Arquillian.class)
public class GuestServiceArquillianIntegrationIT {
    
    @Inject
    private GuestService guestService;
    
    @Deployment
    public static JavaArchive createMicroDeployment() {
        JavaArchive archiv = ShrinkWrap.create(JavaArchive.class)
                .addClass(GuestService.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        return archiv;
    }
    
    @Test
    public void testSimpleScenario() {
        assertNotNull(this.guestService);
        List<Guest> guests = this.guestService.findAllGuests();
        Assert.assertThat(guests, is(not(nullValue())));        
    }            

    
}

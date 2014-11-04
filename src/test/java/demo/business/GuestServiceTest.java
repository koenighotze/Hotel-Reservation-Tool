

package demo.business;

import demo.domain.Guest;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.*;
import org.junit.After;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author dschmitz
 */
public class GuestServiceTest extends AbstractBasePersistenceTest {
    private GuestService guestService;

    private static final Long WELL_KNOWN_ID = 9999L;

    @Override
    protected void initHook() {
        this.guestService = new GuestService();
        this.guestService.setEntityManager(getEntityManager());
    }

    
    public String readFile(String fname) {
        try (Scanner scanner = new Scanner(getClass().getResourceAsStream(fname), "UTF-8")) {
            return scanner.useDelimiter("\\A").next();
        }
    }

    @Override
    protected void cleanupHook() {
        this.guestService = null;
    }
    
    @Test
    public void testFindAllGuests() {
        assertThat(guestService.findAllGuests(), is(not(nullValue())));
    }
    
    @Test
    public void testAddGuest() {                        
        Guest guest = new Guest("name", "email@foo.de");
        
        this.guestService.saveGuest(guest);
        getEntityManager().flush(); // fore id pickup
        
        assertThat(guest.getId(), is(not(nullValue())));
    }
    
    public void testfindAllGuestsEmpty() {
        
    }
   
    public void testfindAllGuestsNonEmpty() {
        
    }
    
    @Test
    public void testUpdateGuest() {
        final String newName = "Bratislav Metulski";
        
        Guest guest = this.guestService.findById(WELL_KNOWN_ID);
        assertThat("Well known guest not found!", guest, is(not(nullValue())));
        assertThat("Name should not be " + newName, guest.getName(), is(not(equalTo(newName))));
       
        guest.setName(newName);
        getEntityManager().detach(guest);
        
        Guest updated = this.guestService.updateGuestDetails(guest);
        getEntityManager().flush();
        
        assertThat("Name should be " + newName, updated.getName(), is(equalTo(newName)));
        
        assertTrue("Version should be incremented ", updated.getVersion() > guest.getVersion());
    }
}

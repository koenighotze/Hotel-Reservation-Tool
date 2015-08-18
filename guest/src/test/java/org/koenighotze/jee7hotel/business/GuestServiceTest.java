

package org.koenighotze.jee7hotel.business;

import org.junit.Test;
import org.koenighotze.jee7hotel.domain.Guest;

import java.util.Optional;
import java.util.Scanner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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
        assertThat(guestService.getAllGuests(), is(not(nullValue())));
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
        
        Optional<Guest> guest = this.guestService.findById(WELL_KNOWN_ID);
        assertThat("Well known guest not found!", guest.get(), is(not(nullValue())));
        assertThat("Name should not be " + newName, guest.get().getName(), is(not(equalTo(newName))));
       
        guest.get().setName(newName);
        getEntityManager().detach(guest.get());
        
        Optional<Guest> updated = this.guestService.updateGuestDetails(guest.get());
        getEntityManager().flush();
        
        assertThat("Name should be " + newName, updated.get().getName(), is(equalTo(newName)));
        
        assertTrue("Version should be incremented ", updated.get().getVersion() > guest.get().getVersion());
    }
}

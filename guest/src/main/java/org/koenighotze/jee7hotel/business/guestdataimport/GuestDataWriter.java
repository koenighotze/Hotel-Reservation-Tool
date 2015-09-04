package org.koenighotze.jee7hotel.business.guestdataimport;

import org.koenighotze.jee7hotel.business.GuestService;
import org.koenighotze.jee7hotel.domain.Guest;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author koenighotze
 */
@Named
@Dependent
public class GuestDataWriter extends AbstractItemWriter {
    private static final Logger LOGGER = Logger.getLogger(GuestDataWriter.class.getName());
    @PersistenceContext
    private EntityManager em;

    private GuestService guestService = new GuestService();

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    public GuestService getGuestService() {
        return guestService;
    }

    public void setGuestService(GuestService guestService) {
        this.guestService = guestService;
    }

    @Override
    @Transactional
    public void writeItems(List<Object> list) throws Exception {
        LOGGER.info(() -> "Writing items " + list + " using guestservice " + this.guestService);

        // TODO: check in spec: why can't I inject Stateless beans into a batch?
        this.guestService.setEntityManager(this.em);

        list.stream()
                .peek(e -> LOGGER.info("Writing " + e))
                .forEach(e -> this.guestService.saveGuest((Guest) e));
    }
}

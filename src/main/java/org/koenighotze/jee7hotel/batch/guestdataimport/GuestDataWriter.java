package org.koenighotze.jee7hotel.batch.guestdataimport;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by dschmitz on 28.11.14.
 */
@Named
@Dependent
public class GuestDataWriter extends AbstractItemWriter {
    private static final Logger LOGGER = Logger.getLogger(GuestDataWriter.class.getName());
    private EntityManager entityManager;

    @Override
    public void writeItems(List<Object> list) throws Exception {
        LOGGER.info(() -> "Writing items " + list);
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}

package org.koenighotze.jee7hotel.batch.guestdataimport;

import javax.batch.api.chunk.ItemProcessor;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.logging.Logger;

/**
 * Created by dschmitz on 28.11.14.
 */
@Named
@Dependent
public class GuestDataProcessor implements ItemProcessor {


    private static final Logger LOGGER = Logger.getLogger(GuestDataProcessor.class.getName());
    @Inject
    private JobContext jobCtx;

    @Override
    public Object processItem(Object o) throws Exception {
        LOGGER.info(() -> "Processing item " + o);

        return o;
    }
}

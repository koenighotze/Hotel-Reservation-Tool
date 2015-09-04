package org.koenighotze.jee7hotel.business.guestdataimport;

import org.koenighotze.jee7hotel.domain.Guest;

import javax.batch.api.chunk.ItemProcessor;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.logging.Logger;

/**
 * @author koenighotze
 */
@Named
@Dependent
public class GuestDataProcessor implements ItemProcessor {
    private static final Logger LOGGER = Logger.getLogger(GuestDataProcessor.class.getName());

    private JobContext jobCtx;

    public GuestDataProcessor() {
    }

    @Inject
    public GuestDataProcessor(JobContext jobContext) {
        this.jobCtx = jobContext;
    }

    @Override
    public Object processItem(Object o) throws Exception {
        LOGGER.info(() -> "Processing item " + o);

        String[] split = ((String) o).split(",");

        if (split.length != 2) {
            throw new IllegalArgumentException("Unexpected format in " + o);
        }

        String name = split[0].trim();
        String email = split[1].trim();

        if (!email.matches("\\p{Alnum}+@\\p{Alnum}+.\\p{Alnum}+")) {
            throw new IllegalArgumentException("Unexpected email '" + email + "'");
        }

        return new Guest(name, email);
    }
}

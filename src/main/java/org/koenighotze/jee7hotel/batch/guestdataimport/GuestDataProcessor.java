package org.koenighotze.jee7hotel.batch.guestdataimport;

import org.koenighotze.jee7hotel.domain.Guest;

import javax.batch.api.chunk.ItemProcessor;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.StringTokenizer;
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

        String[] split = ((String) o).split(",");

        if (split.length != 2) {
            throw new IllegalArgumentException("Unexpected format in " + o);
        }

        String name = split[0].trim();
        String email = split[1].trim();

        if (!email.matches("\\p{Alnum}+@\\p{Alnum}+.\\p{Alnum}+")) {
            throw new IllegalArgumentException("Unexpected email '" + email + "'");
        }

        Guest guest = new Guest(name, email);
        return guest;
    }
}

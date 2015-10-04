package org.koenighotze.jee7hotel.guest.business.guestdataimport;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author koenighotze
 */
@Named
@RequestScoped
public class GuestDataImportBatchBean {
    private static final Logger LOGGER = Logger.getLogger(GuestDataImportBatchBean.class.getName());

    @SuppressWarnings("unused")
    public void importGuestData(@NotNull String resourceName) {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Properties props = new Properties();
        props.put("resourceName", resourceName);

        LOGGER.info("Calling batch with configuration " + props);
        jobOperator.start("guest-import", props);
    }
}

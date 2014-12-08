package org.koenighotze.jee7hotel.batch.guestdataimport;


import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by dschmitz on 28.11.14.
 */
@Named
@RequestScoped
public class GuestDataImportBatchBean {

    private static final Logger LOGGER = Logger.getLogger(GuestDataImportBatchBean.class.getName());

    public void importGuestData(@NotNull String resourceName) {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Properties props = new Properties();
        props.put("resourceName", resourceName);

        LOGGER.info("Calling batch with configuration " + props);
        jobOperator.start("guest-import", props);
    }
}

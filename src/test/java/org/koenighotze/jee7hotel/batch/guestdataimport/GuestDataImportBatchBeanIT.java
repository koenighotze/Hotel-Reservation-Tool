package org.koenighotze.jee7hotel.batch.guestdataimport;

import com.sun.xml.ws.api.tx.at.Transactional;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.base.asset.AssetUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.jee7hotel.domain.Guest;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.EnumSet;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.*;
import static org.koenighotze.jee7hotel.business.integration.BaseArquillianSetup.*;

import static org.junit.Assert.*;


@RunWith(Arquillian.class)
public class GuestDataImportBatchBeanIT {
    private static final Logger LOGGER = Logger.getLogger(GuestDataImportBatchBeanIT.class.getName());


    @Inject
    private GuestDataImportBatchBean guestDataImportBatchBean;

    @PersistenceContext
    private EntityManager entityManager;

    @Deployment
    public static WebArchive setup() {
        WebArchive deployment = createBaseDeployment();

        String dataCsv = AssetUtil
                .getClassLoaderResourceName(GuestDataImportBatchBeanIT.class.getPackage(),
                        "integration-guest-data.csv");

        deployment.addPackage(GuestDataImportBatchBean.class.getPackage())
                .addAsResource("META-INF/batch-jobs/guest-import.xml")
                .addAsResource(dataCsv, "META-INF/guest-data.csv");

        return deployment;

    }

    @Test
    @Transactional
    public void testBatch() throws InterruptedException {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Properties props = new Properties();
        props.put("resourceName", "guest-data.csv");

        LOGGER.info("Starting batch");
        long jobId = jobOperator.start("guest-import", props);

        JobExecution jobExecution = jobOperator.getJobExecution(jobId);

        LOGGER.info("....waiting for batch to finish");
        EnumSet<BatchStatus> statuses = EnumSet.of(BatchStatus.STARTED, BatchStatus.STARTING);
        while (statuses.contains(jobExecution.getBatchStatus())) {
            Thread.sleep(1000L);
        }

        assertEquals(BatchStatus.COMPLETED, jobExecution.getBatchStatus());

        Guest guest = this.entityManager.find(Guest.class, 123L);
        assertThat("Expected to find a guest with id "+ 123L, guest, is(not(nullValue())));
    }
}

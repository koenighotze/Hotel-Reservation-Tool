package org.koenighotze.jee7hotel.business.guestdataimport;

import com.sun.xml.ws.api.tx.at.Transactional;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.base.asset.AssetUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.jee7hotel.business.GuestService;
import org.koenighotze.jee7hotel.business.logging.PerformanceLogger;
import org.koenighotze.jee7hotel.domain.Guest;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.EnumSet;
import java.util.Properties;
import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


@RunWith(Arquillian.class)
public class GuestDataImportBatchBeanIT {
    private static final Logger LOGGER = Logger.getLogger(GuestDataImportBatchBeanIT.class.getName());


    @Inject
    private GuestDataImportBatchBean guestDataImportBatchBean;

    @PersistenceContext
    private EntityManager entityManager;

    @Deployment
    public static WebArchive setup() {
        WebArchive deployment = BaseArquillianSetup.createBaseDeployment();

        String dataCsv = AssetUtil
                .getClassLoaderResourceName(GuestDataImportBatchBeanIT.class.getPackage(),
                        "integration-guest-data.csv");

        deployment
                .addPackages(false,
                        BaseArquillianSetup::excludeTest,
                        GuestDataImportBatchBean.class.getPackage())
                .addClass(GuestService.class)
                .addClass(PerformanceLogger.class)
                .addAsResource("META-INF/batch-jobs/guest-import.xml")
                .addAsResource(dataCsv, "META-INF/guest-data.csv")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        LOGGER.info(deployment.toString(Formatters.VERBOSE));
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

        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Guest> cq = cb.createQuery(Guest.class);
        Root<Guest> from = cq.from(Guest.class);
        cq.select(from).where(cb.equal(from.get("name"), "Schmi"));

        Guest guestFromDb = this.entityManager.createQuery(cq).getSingleResult();

        assertThat("Expected to find a guest with name " + "Schmi", guestFromDb, is(not(nullValue())));
    }
}

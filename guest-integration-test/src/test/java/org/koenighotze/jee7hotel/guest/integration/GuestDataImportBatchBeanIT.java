package org.koenighotze.jee7hotel.guest.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.base.asset.AssetUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.jee7hotel.framework.integration.BaseArquillianSetup;
import org.koenighotze.jee7hotel.guest.business.guestdataimport.GuestDataImportBatchBean;

import javax.batch.operations.JobOperator;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.logging.Logger;

import static javax.batch.runtime.BatchRuntime.getJobOperator;

@RunWith(Arquillian.class)
public class GuestDataImportBatchBeanIT {
    private static final Logger LOGGER = Logger.getLogger(GuestDataImportBatchBeanIT.class.getName());

    @Inject
    private GuestDataImportBatchBean guestDataImportBatchBean;

    //
//    @PersistenceContext
//    private EntityManager entityManager;
//
    @Deployment
    public static WebArchive setup() {
        WebArchive deployment = BaseArquillianSetup.createStandardDeployment(GuestDataImportBatchBeanIT.class.getPackage());

        String dataCsv = AssetUtil
                .getClassLoaderResourceName(GuestDataImportBatchBeanIT.class.getPackage(),
                        "integration-guest-data.csv");

        deployment
                .addAsResource("META-INF/batch-jobs/guest-import.xml")
                .addAsResource(dataCsv, "META-INF/guest-data.csv");

        LOGGER.info(deployment.toString(Formatters.VERBOSE));
        return deployment;
    }

    @Test
    @Transactional
    public void guest_are_imported_using_a_batch() throws InterruptedException {
        JobOperator jobOperator = getJobOperator();
//
//        Properties props = new Properties();
//        props.put("resourceName", "guest-data.csv");
//
//        LOGGER.info("Starting batch");
//        long jobId = jobOperator.start("guest-import", props);
//
//        JobExecution jobExecution = jobOperator.getJobExecution(jobId);
//
//        LOGGER.info("....waiting for batch to finish");
//        EnumSet<BatchStatus> statuses = EnumSet.of(BatchStatus.STARTED, BatchStatus.STARTING);
//        while (statuses.contains(jobExecution.getBatchStatus())) {
//            Thread.sleep(1000L);
//        }
//
//        assertEquals(BatchStatus.COMPLETED, jobExecution.getBatchStatus());
//
//        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
//        CriteriaQuery<Guest> cq = cb.createQuery(Guest.class);
//        Root<Guest> from = cq.from(Guest.class);
//        cq.select(from).where(cb.equal(from.get("name"), "Schmi"));
//
//        Guest guestFromDb = this.entityManager.createQuery(cq).getSingleResult();
//
//        assertThat("Expected to find a guest with name " + "Schmi", guestFromDb, is(not(nullValue())));
    }
}

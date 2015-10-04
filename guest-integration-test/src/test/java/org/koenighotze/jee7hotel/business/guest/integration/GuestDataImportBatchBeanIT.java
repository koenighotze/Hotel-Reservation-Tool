package org.koenighotze.jee7hotel.business.guest.integration;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.logging.Logger;


@RunWith(Arquillian.class)
public class GuestDataImportBatchBeanIT {
    private static final Logger LOGGER = Logger.getLogger(GuestDataImportBatchBeanIT.class.getName());

//
//    @Inject
//    private GuestDataImportBatchBean guestDataImportBatchBean;
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Deployment
//    public static WebArchive setup() {
//        WebArchive deployment = BaseArquillianSetup.createBaseDeployment();
//
//        String dataCsv = AssetUtil
//                .getClassLoaderResourceName(GuestDataImportBatchBeanIT.class.getPackage(),
//                        "integration-guest-data.csv");
//
//        deployment
//                .addPackages(false,
//                        BaseArquillianSetup::excludeTest,
//                        GuestDataImportBatchBean.class.getPackage())
//                .addClass(GuestService.class)
//                .addClass(PerformanceLogger.class)
//                .addAsResource("META-INF/batch-jobs/guest-import.xml")
//                .addAsResource(dataCsv, "META-INF/guest-data.csv")
//                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
//
//        LOGGER.info(deployment.toString(Formatters.VERBOSE));
//        return deployment;
//
//    }
//
//    @Test
//    @Transactional
//    public void testBatch() throws InterruptedException {
//        JobOperator jobOperator = BatchRuntime.getJobOperator();
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
//    }
    @Test
    public void nothing() {

    }
}

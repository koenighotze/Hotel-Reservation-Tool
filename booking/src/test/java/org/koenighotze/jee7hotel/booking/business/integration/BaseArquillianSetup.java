package org.koenighotze.jee7hotel.booking.business.integration;

import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.base.asset.AssetUtil;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.koenighotze.jee7hotel.booking.business.ReservationBackendHandler;
import org.koenighotze.jee7hotel.booking.business.events.ReservationStatusChangeEvent;
import org.koenighotze.jee7hotel.booking.business.json.BookingMessageTOReader;
import org.koenighotze.jee7hotel.booking.domain.Reservation;
import org.koenighotze.jee7hotel.business.eventsource.EventSource;
import org.koenighotze.jee7hotel.business.eventsource.EventSourceInterceptor;
import org.koenighotze.jee7hotel.business.eventsource.IEventSource;
import org.koenighotze.jee7hotel.business.logging.PerformanceLogger;
import org.koenighotze.jee7hotel.business.monitor.ApplicationMonitorBean;

import java.io.File;

/**
 * @author koenighotze
 */
public class BaseArquillianSetup {

    public static boolean excludeTest(ArchivePath path) {
        return !(path.get().endsWith("Test.class") || path.get().endsWith("IT.class"));
    }


    public static WebArchive createFullServiceDeployment() {
        return createBaseDeployment()
                .addPackages(true, BaseArquillianSetup::excludeTest,
                        "org.koenighotze.jee7hotel.domain",
                        "org.koenighotze.jee7hotel.business",
                        "org.koenighotze.jee7hotel.resources")
                .deletePackage("org.koenighotze.jee7hotel.frontend")
                .deletePackage("org.koenighotze.jee7hotel.batch")
                .deletePackage("org.koenighotze.jee7hotel.business.monitor")
                .deleteClass(ApplicationMonitorBean.class);
//                .deleteClass(EventSourceBean.class);
    }


    public static WebArchive createBaseDeployment() {
        String loadSql = AssetUtil
                .getClassLoaderResourceName(BaseArquillianSetup.class.getPackage(),
                        "integration-prepareTestData.sql");
        String persistenceXml = AssetUtil
                .getClassLoaderResourceName(BaseArquillianSetup.class.getPackage(),
                        "integration-test-persistence.xml");

        String jbossWebXml = AssetUtil
                .getClassLoaderResourceName(BaseArquillianSetup.class.getPackage(),
                        "integration-jboss-web.xml");
//
        WebArchive archive = ShrinkWrap.create(WebArchive.class)
                .addAsResource(loadSql, "META-INF/load.sql")
                .addAsResource(persistenceXml, "META-INF/persistence.xml")
                .addAsWebInfResource(BaseArquillianSetup.class.getPackage(),
                        "integration-glassfish-resources.xml",
                        "glassfish-resources.xml")
                .addAsWebInfResource(BaseArquillianSetup.class.getPackage(),
                        "integration-jboss-jms.xml",
                        "jboss-jms.xml")
                .addAsWebInfResource(jbossWebXml, "jboss-web.xml")

                        // every IT needs the domain
                .addPackages(true, BaseArquillianSetup::excludeTest,
                        Reservation.class.getPackage())

                        // every IT needs the interceptors
                .addPackages(true, BaseArquillianSetup::excludeTest,
                        PerformanceLogger.class.getPackage())

                .addClasses(EventSourceInterceptor.class,
//                        LoggingEventSourceBean.class,
//                        Event.class,
                        IEventSource.class,
                        EventSource.class)
                .deleteClass(ReservationBackendHandler.class)

                .addClass(BaseArquillianSetup.class)
                .deleteClass(ApplicationMonitorBean.class)
                        // ...and the events
                .addPackages(true, BaseArquillianSetup::excludeTest,
                        ReservationStatusChangeEvent.class.getPackage())
                        // ...and the JSON Support
                .addPackages(true, BaseArquillianSetup::excludeTest,
                        BookingMessageTOReader.class.getPackage());

        File[] libs = Maven
                .resolver()
                .loadPomFromFile("pom.xml")
                .importRuntimeDependencies()
                .resolve()
                .withTransitivity()
                .asFile();
        archive.addAsLibraries(libs);


        return archive;
    }


}

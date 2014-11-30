package org.koenighotze.jee7hotel.business.integration;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.base.asset.AssetUtil;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.koenighotze.jee7hotel.business.events.ReservationStatusChangeEvent;
import org.koenighotze.jee7hotel.business.eventsource.EventSourceBean;
import org.koenighotze.jee7hotel.business.json.BookingMessageTOReader;
import org.koenighotze.jee7hotel.business.logging.PerformanceLogger;
import org.koenighotze.jee7hotel.domain.Reservation;

import java.io.File;

/**
 * Created by dschmitz on 24.11.14.
 */
public class BaseArquillianSetup {
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
                .addPackages(true, Reservation.class.getPackage())
                        // every IT needs the interceptors
                .addPackages(true, PerformanceLogger.class.getPackage())
                .addPackages(true, EventSourceBean.class.getPackage())
                // in general do not use the mongodb backend
                .deleteClass(EventSourceBean.class)
                        // ...and the events
                .addPackages(true, ReservationStatusChangeEvent.class.getPackage())

                        // ...and the JSON Support
                .addPackages(true, BookingMessageTOReader.class.getPackage());

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

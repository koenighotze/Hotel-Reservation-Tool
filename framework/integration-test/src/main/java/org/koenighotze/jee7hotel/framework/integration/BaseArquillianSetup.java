package org.koenighotze.jee7hotel.framework.integration;

import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.base.asset.AssetUtil;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static org.fest.assertions.Assertions.assertThat;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.formatter.Formatters.VERBOSE;
import static org.jboss.shrinkwrap.resolver.api.maven.Maven.resolver;

/**
 * Base setup for arquillian integration tests.
 *
 * @author koenighotze
 */
public class BaseArquillianSetup {
    private static final Logger LOGGER = getLogger(BaseArquillianSetup.class.getName());
    public static final String INTEGRATION_JBOSS_WEB_XML = "integration-jboss-web.xml";

    public static boolean excludeTest(@NotNull ArchivePath path) {
        return !(path.get().endsWith("Test.class") || path.get().endsWith("IT.class"));
    }

    /**
     * Reads the JBoss Web Descriptor
     * @param resourcePackage the base package
     * @return the Web.xml
     */
    public static String readJBossWebXml(@NotNull Package resourcePackage) {
        String jbossWebXml = AssetUtil
                .getClassLoaderResourceName(resourcePackage,
                        INTEGRATION_JBOSS_WEB_XML);
        assertThat(jbossWebXml).isNotNull();
        return jbossWebXml;
    }

    /**
     * Builds the list of all transitive runtime dependencies.
     *
     * @return the filelist
     */
    public static File[] buildMavenLibraryDependencies() {
        return resolver()
                .loadPomFromFile("pom.xml")
                .importRuntimeDependencies()
                .resolve()
                .withTransitivity()
                .asFile();
    }

    /**
     * Builds the standard deployment including meta descriptors
     * and all runtime dependencies.
     *
     * @param referencePackage the reference package
     * @return the Webarchive
     */
    public static WebArchive createStandardDeployment(@NotNull Package referencePackage) {
        try {
            String jbossWebXml = readJBossWebXml(referencePackage);

            File[] libs = buildMavenLibraryDependencies();

            WebArchive baseDeployment = create(WebArchive.class)
                    .addAsWebInfResource(jbossWebXml, "jboss-web.xml")
                    .addAsLibraries(libs);

            LOGGER.fine(() -> baseDeployment.toString(VERBOSE));
            return baseDeployment;
        } catch (Exception e) {
            LOGGER.log(SEVERE, e, () -> "Creating deployment failed");
            throw e;
        }
    }
}

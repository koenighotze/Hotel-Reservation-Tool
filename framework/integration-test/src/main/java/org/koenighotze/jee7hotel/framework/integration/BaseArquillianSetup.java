package org.koenighotze.jee7hotel.framework.integration;

import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.base.asset.AssetUtil;

import java.io.File;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;
import static org.fest.assertions.Assertions.assertThat;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.formatter.Formatters.VERBOSE;
import static org.jboss.shrinkwrap.resolver.api.maven.Maven.resolver;

/**
 * @author koenighotze
 */
// TODO clean up the mess
public class BaseArquillianSetup {
    private static final Logger LOGGER = Logger.getLogger(BaseArquillianSetup.class.getName());

    public static boolean excludeTest(ArchivePath path) {
        return !(path.get().endsWith("Test.class") || path.get().endsWith("IT.class"));
    }

    public static String readJBossWebXml(Package resourcePackage) {
        String jbossWebXml = AssetUtil
                .getClassLoaderResourceName(resourcePackage,
                        "integration-jboss-web.xml");
        assertThat(jbossWebXml).isNotNull();
        return jbossWebXml;
    }

    public static File[] buildMavenLibraryDependencies() {
        return resolver()
                .loadPomFromFile("pom.xml")
                .importRuntimeDependencies()
                .resolve()
                .withTransitivity()
                .asFile();
    }

    public static WebArchive createStandardDeployment(Package referencePackage) {
        try {
            String jbossWebXml = readJBossWebXml(referencePackage);

            File[] libs = buildMavenLibraryDependencies();

            WebArchive baseDeployment = create(WebArchive.class)
                    .addAsWebInfResource(jbossWebXml, "jboss-web.xml")
                    .addAsLibraries(libs);

            LOGGER.info(() -> baseDeployment.toString(VERBOSE));
            return baseDeployment;
        } catch (Exception e) {
            LOGGER.log(SEVERE, e, () -> "Creating deployment failed");
            throw e;
        }
    }
}

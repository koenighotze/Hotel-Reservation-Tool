package org.koenighotze.jee7hotel.business.guest.integration;

import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.impl.base.asset.AssetUtil;

import java.io.File;

import static org.fest.assertions.Assertions.assertThat;
import static org.jboss.shrinkwrap.resolver.api.maven.Maven.resolver;

/**
 * @author koenighotze
 */
// TODO: Oh ha...copy paste
public class BaseArquillianSetup {

    public static boolean excludeTest(ArchivePath path) {
        return !(path.get().endsWith("Test.class") || path.get().endsWith("IT.class"));
    }

    protected static String readJBossWebXml(Package resourcePackage) {
        String jbossWebXml = AssetUtil
                .getClassLoaderResourceName(resourcePackage,
                        "integration-jboss-web.xml");
        assertThat(jbossWebXml).isNotNull();
        return jbossWebXml;
    }

    protected static File[] buildMavenLibraryDependencies() {
        return resolver()
                .loadPomFromFile("pom.xml")
                .importRuntimeDependencies()
                .resolve()
                .withTransitivity()
                .asFile();
    }
}

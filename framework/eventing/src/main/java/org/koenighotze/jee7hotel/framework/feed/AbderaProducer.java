package org.koenighotze.jee7hotel.framework.feed;

import org.apache.abdera.Abdera;

import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * @author dschmitz
 */

@Named
@Singleton
public class AbderaProducer {
    private final Abdera abdera = new Abdera();

    @Produces
    public Abdera abdera() {
        return abdera;
    }
}

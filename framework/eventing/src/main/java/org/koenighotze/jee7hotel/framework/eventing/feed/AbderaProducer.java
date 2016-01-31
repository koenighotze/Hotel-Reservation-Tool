package org.koenighotze.jee7hotel.framework.eventing.feed;

import org.apache.abdera.Abdera;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * @author dschmitz
 */

@Named
@Singleton
public class AbderaProducer {
    private Abdera abdera;

    @PostConstruct
    public void init() {
        abdera = new Abdera();


    }

    @Produces
    public Abdera abdera() {
        return abdera;
    }
}

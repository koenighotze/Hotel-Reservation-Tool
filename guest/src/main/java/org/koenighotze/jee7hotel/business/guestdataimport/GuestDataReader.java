package org.koenighotze.jee7hotel.business.guestdataimport;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Created by dschmitz on 28.11.14.
 */
@Named
@Dependent
public class GuestDataReader extends AbstractItemReader {

    private static final Logger LOGGER = Logger.getLogger(GuestDataProcessor.class.getName());
    @Inject
    private JobContext jobCtx;

    private BufferedReader reader;

    void setReader(BufferedReader reader) {
        this.reader = reader;
    }


    @Inject
    @BatchProperty(name = "resourceNameDefault")
    private String resourceNameDefault;

    @Override
    public void close() throws Exception {
        this.reader.close();
    }

    @Override
    public void open(Serializable checkpoint) throws Exception {
        String resourceName = BatchRuntime
                .getJobOperator()
                .getParameters(this.jobCtx.getExecutionId())
                .getProperty("resourceName");

        if (null == resourceName) {
            resourceName = this.resourceNameDefault;
        }

        if (null == resourceName) {
            throw new NullPointerException("'resourceName' is not set!");
        }

        LOGGER.info("Opening " + resourceName);

        this.reader = new BufferedReader(
                new InputStreamReader(
                        Thread.currentThread()
                                .getContextClassLoader()
                                .getResourceAsStream("META-INF/" + resourceName)));
    }

    @Override
    public Object readItem() throws Exception {
        Object obj = this.reader.readLine();
        LOGGER.fine(() -> "Read " + obj);
        return obj;
    }
}

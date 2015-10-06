package org.koenighotze.jee7hotel.framework.application.monitor;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Fake application status.
 *
 * @author koenighotze
 */
@XmlRootElement
public class ApplicationStatus implements Serializable {
    private boolean isUp = true;

    private long numberOfServiceCalls = 20;

    private long averageDurationInMillis = 1234L;

    public boolean isUp() {
        return isUp;
    }

    public void setUp(boolean isUp) {
        this.isUp = isUp;
    }

    public long getNumberOfServiceCalls() {
        return numberOfServiceCalls;
    }

    public void setNumberOfServiceCalls(long numberOfServiceCalls) {
        this.numberOfServiceCalls = numberOfServiceCalls;
    }

    public long getAverageDurationInMillis() {
        return averageDurationInMillis;
    }

    public void setAverageDurationInMillis(long averageDurationInMillis) {
        this.averageDurationInMillis = averageDurationInMillis;
    }
}



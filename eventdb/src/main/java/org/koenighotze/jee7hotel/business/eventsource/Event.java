package org.koenighotze.jee7hotel.business.eventsource;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.mongojack.Id;
import org.mongojack.ObjectId;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by dschmitz on 28.11.14.
 */
@XmlRootElement
//@JsonIgnoreProperties(ignoreUnknown=true)
public class Event implements Serializable {
    @ObjectId
    @Id
    public String id;

    @JsonProperty
    private String className;
    @JsonProperty
    private String methodName;
    @JsonProperty
    private Long millis;
    @JsonProperty
    private Object payload;

    public Event() {
    }

    public Event(String className, String methodName, Long millis, Object payload) {
        this.className = className;
        this.methodName = methodName;
        this.millis = millis;
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", millis=" + millis +
                ", payload=" + payload +
                '}';
    }
}

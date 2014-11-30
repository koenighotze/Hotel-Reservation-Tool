package org.koenighotze.jee7hotel.business.eventsource;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
* Created by dschmitz on 28.11.14.
*/
@XmlRootElement
public class Event implements Serializable {
    private final String className;
    private final String methodName;
    private final Long millis;
    private final Object payload;

    public Event(String className, String methodName, Long millis, Object payload) {
        this.className = className;
        this.methodName = methodName;
        this.millis = millis;
        this.payload = payload;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }


    public static Event fromJson(String json) {
        JsonParser parser = new JsonParser();
        JsonElement parse = parser.parse(json);

        return new Gson().fromJson(parse, Event.class);
    }

    @Override
    public String toString() {
        return "Event{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", millis=" + millis +
                ", payload=" + payload +
                '}';
    }
}

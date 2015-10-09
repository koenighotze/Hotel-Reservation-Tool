package org.koenighotze.jee7hotel.guest.frontend;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

/**
 * @author dschmitz
 */
@XmlRootElement
public class Message {
    private String key;
    private String value;

    public Message() {
    }

    public Message(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("key", key)
                .append("value", value)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Message)) {
            return false;
        }

        Message other = (Message) o;
        return Objects.equals(other.key, key) && Objects.equals(other.value, value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}

package org.koenighotze.jee7hotel.booking.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

/**
 * Local model of guest data.
 *
 * @author dschmitz
 */
@XmlRootElement(name = "Guest")
public class GuestModel {

    private String publicId;
    private String name;

    public GuestModel() {
    }

    public GuestModel(String publicId, String lastName) {
        this.publicId = publicId;
        this.name = lastName;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicId, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GuestModel)) {
            return false;
        }

        GuestModel other = (GuestModel) obj;
        return Objects.equals(getName(), other.getName()) && Objects.equals(getPublicId(), other.getPublicId());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("publicId", publicId)
                .append("name", name)
                .toString();
    }
}

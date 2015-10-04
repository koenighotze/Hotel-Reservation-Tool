

package org.koenighotze.jee7hotel.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.koenighotze.jee7hotel.framework.application.jaxb.DateAdapter;
import org.koenighotze.jee7hotel.framework.persistence.audit.Audit;
import org.koenighotze.jee7hotel.framework.persistence.audit.Auditable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import static java.time.ZoneId.of;
import static java.time.ZoneOffset.UTC;
import static java.util.Date.from;

/**
 * @author dschmitz
 */
@XmlRootElement
@Entity
@Table(name = "GUEST") //, schema = "JEE7_DEMO") 
@NamedQueries({
        @NamedQuery(
                name = Guest.GUEST_FIND_BY_NAME,
                query = "select g from Guest g where g.name = :name"),
        @NamedQuery(
                name = Guest.GUEST_FIND_BY_PUBLIC_ID,
                query = "select g from Guest g where g.publicId = :publicId")
})
@EntityListeners(Audit.class)
public class Guest implements Serializable {
    public static final String GUEST_FIND_BY_PUBLIC_ID = "Guest.findByPublicId";
    public static final String GUEST_FIND_BY_NAME = "Guest.findByName";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @NotNull
    private String name;

    private String email;

    @NotNull
    private String publicId;

    @Auditable
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date lastUpdateTimestamp;

    public Guest() {
    }

    public Guest(@NotNull String publicId, @NotNull String name, String email) {
        this.name = name;
        this.email = email;
        this.publicId = publicId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @XmlTransient
    @Transient
    public LocalDateTime getLastUpdate() {
        if (null == lastUpdateTimestamp) {
            return null;
        }
        return LocalDateTime.from(lastUpdateTimestamp.toInstant().atZone(of(UTC.getId())));
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        if (null == lastUpdate) {
            this.lastUpdateTimestamp = null;
        } else {
            this.lastUpdateTimestamp = from(lastUpdate.toInstant(UTC));
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("version", version)
                .append("publicId", publicId)
                .append("email", email)
                .append("name", name)
                .append("lastUpdateTimestamp", lastUpdateTimestamp)
                .toString();
    }

    public static Guest nullGuest() {
        return new Guest("", "", "");
    }
}

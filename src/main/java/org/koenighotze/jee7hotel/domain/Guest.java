

package org.koenighotze.jee7hotel.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author dschmitz
 */
@XmlRootElement
@Entity
@Table(name = "GUEST") //, schema = "JEE7_DEMO") 
@NamedQueries(
        @NamedQuery(
                name = "Guest.findByName", query = "select g from Guest g where g.name = :name")
)
public class Guest implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Version
    private Long version;
    
    @NotNull
    private String name;
       
    private String email;

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

    Guest() {
    }
    
    public Guest(@NotNull String name, String email) {
        this.name = name;
        this.email = email;
    }
    
    @Override
    public String toString() {
        return "Guest{" + "id=" + id + ", version=" + version + ", name=" + name + ", email=" + email + '}';
    }
    
    
}

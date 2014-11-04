package demo.pocs;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;



/**
 *
 * @author dschmitz
 */
@Named
@RequestScoped
public class TestBean {
    private String id;

    public String getId() {
        
        return id;
    }

    public Long calc() {
        return System.currentTimeMillis();
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    
}

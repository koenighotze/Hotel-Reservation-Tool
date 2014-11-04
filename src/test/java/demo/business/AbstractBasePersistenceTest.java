

package demo.business;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.Before;

/**
 *
 * @author dschmitz
 */
public class AbstractBasePersistenceTest {    
    private EntityManager em;
    
    private EntityTransaction transaction;

    
    @Before
    public final void initializeDependencies(){
        this.em = Persistence.createEntityManagerFactory("integration-test").createEntityManager();
        this.transaction = this.em.getTransaction();
        this.transaction.begin();
        
        initHook();
    }
    
    @After
    public final void cleanup() {
        cleanupHook();        
        
        if (null != this.transaction) {
            this.transaction.rollback();
        }
        if (null != this.em) {
            this.em.close();
        }                
    }
    
    protected EntityManager getEntityManager() {
        return em;
    }

    protected EntityTransaction getTransaction() {
        return transaction;
    }
    
    
    protected void initHook() {
        
    }

    protected void cleanupHook() {
    }
}

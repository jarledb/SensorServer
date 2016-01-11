package no.stonehill;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDbTest {

    protected static EntityManagerFactory entityManagerFactory;
    protected EntityManager em;

    @BeforeClass
    public static void setUp() throws Exception {
        Map properties = new HashMap<String, String>();
        entityManagerFactory = Persistence.createEntityManagerFactory("sensor-service-test", properties);
    }

    @AfterClass
    public static void closeEntityManagerFactory() {
        entityManagerFactory.close();
    }

    @Before
    public void beginTransaction() {
        em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
    }

    @After
    public void rollbackTransaction() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }

        if (em.isOpen()) {
            em.close();
        }
    }


}

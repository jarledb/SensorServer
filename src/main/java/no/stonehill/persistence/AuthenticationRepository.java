package no.stonehill.persistence;

import no.stonehill.domain.Apiuser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

@Repository
public class AuthenticationRepository {
    private static Logger LOG = LoggerFactory.getLogger(AuthenticationRepository.class);

    @PersistenceContext
    private EntityManager em;
    private EntityManagerFactory entityManagerFactory;

    public AuthenticationRepository() {
        LOG.info("**************AuthenticationRepository init************");
        //em = Persistence.createEntityManagerFactory("sensor_service").createEntityManager();

//        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
//        entityManagerFactory = (EntityManagerFactory) context.getBean("emf");
    }

    @PersistenceUnit(unitName = "entityManagerFactory")
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        LOG.info("**************EMF init************");
        this.entityManagerFactory = emf;
        //this.em = entityManagerFactory.createEntityManager();
    }

//    @PersistenceContext(unitName = "sensor_service", type = PersistenceContextType.EXTENDED)
//    public void setEntityManager(EntityManager entityManager) {
//        LOG.error("\n\n**************EM init************\n\n");
//        this. em = entityManager;
//    }

    public Apiuser fetchUser(Long id) {
        LOG.info("Fetching uder with id: " + id);
        return em.find(Apiuser.class, id);
    }

    @Transactional
    @javax.transaction.Transactional
    public Apiuser persist(Apiuser apiuser) {
        LOG.info("Persisting apiuser: " + apiuser.toString());
        em.merge(apiuser);
        Apiuser apiuser1 = em.find(Apiuser.class, apiuser.getId());
        LOG.info("Done persisting: " + apiuser1);
        return apiuser1;
    }

}

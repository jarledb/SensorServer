package no.stonehill.persistence;

import no.stonehill.domain.Apiuser;
import no.stonehill.domain.SensorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

@Repository
public class AuthenticationRepository {
    private static Logger LOG = LoggerFactory.getLogger(AuthenticationRepository.class);

    @PersistenceContext
    private EntityManager em;
    private EntityManagerFactory entityManagerFactory;

    @PersistenceUnit(unitName = "entityManagerFactory")
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        LOG.info("**************EMF init************");
        this.entityManagerFactory = emf;
    }

    public Apiuser fetchUser(Long id) {
        LOG.info("Fetching uder with id: " + id);
        return em.find(Apiuser.class, id);
    }

    @Transactional
    @javax.transaction.Transactional
    public Apiuser persist(Apiuser apiuser) {
        LOG.info("Persisting apiuser: " + apiuser.toString());
        return em.merge(apiuser);
    }

    public Apiuser getApiUserbyName(String username) {
        return em.createQuery("Select a From Apiuser a where name = :name", Apiuser.class)
                .setParameter("name", username)
                .getSingleResult();
    }

    public SensorEvent persist(SensorEvent event) {
        LOG.info("Persisting event: " + event.toString());
        return em.merge(event);
    }

    public List<SensorEvent> fetchAllEvents() {
        return em.createQuery("From SensorEvent", SensorEvent.class).getResultList();
    }
}

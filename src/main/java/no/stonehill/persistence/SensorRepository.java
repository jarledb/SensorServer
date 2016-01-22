package no.stonehill.persistence;

import no.stonehill.domain.Sensor;
import no.stonehill.domain.SensorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

@Repository
public class SensorRepository {
    private static Logger LOG = LoggerFactory.getLogger(SensorRepository.class);

    @PersistenceContext
    private EntityManager em;
    private EntityManagerFactory entityManagerFactory;

    @PersistenceUnit(unitName = "entityManagerFactory")
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.entityManagerFactory = emf;
    }

    public SensorEvent persist(SensorEvent event) {
        LOG.info("Persisting sensorevent: " + event.toString());
        return em.merge(event);
    }

    public Sensor persist(Sensor sensor) {
        LOG.info("Persisting sensor: " + sensor.toString());
        return em.merge(sensor);
    }

    public List<SensorEvent> fetchAllEvents() {
        return em.createQuery("From SensorEvent", SensorEvent.class).getResultList();
    }

    public List<Sensor> fetchAllSensors(String typeFilter) {
        if (typeFilter == null || typeFilter.isEmpty()) {
            return em.createQuery("From Sensor", Sensor.class).getResultList();
        } else {
            return em.createQuery("From Sensor WHERE type=:type", Sensor.class).setParameter("type", typeFilter).getResultList();
        }
    }

    public Sensor fetchSensor(long id) {
        return em.find(Sensor.class, id);
    }
}

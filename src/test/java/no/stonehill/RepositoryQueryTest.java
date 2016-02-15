package no.stonehill;

import no.stonehill.domain.Apiuser;
import no.stonehill.domain.Sensor;
import no.stonehill.domain.SensorEvent;
import no.stonehill.persistence.AuthenticationRepository;
import no.stonehill.persistence.SensorRepository;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class RepositoryQueryTest extends AbstractDbTest {
    SensorRepository repo;
    AuthenticationRepository authenticationRepository;

    public static final String TEST_VALUE = "testValue";
    public static final String TESTKEY = "testkey";
    public static final String TEST_VALUE_2 = "testValue2";

    @Before
    public void init() throws NoSuchFieldException, IllegalAccessException {
        Field f = SensorRepository.class.getDeclaredField("em");
        repo = new SensorRepository();
        f.setAccessible(true);
        f.set(repo, em);

        Field arf = AuthenticationRepository.class.getDeclaredField("em");
        authenticationRepository = new AuthenticationRepository();
        arf.setAccessible(true);
        arf.set(authenticationRepository, em);
    }

    @Test
    public void testSaveAndFetchUser() {
        Apiuser toSave = new Apiuser();
        toSave.setId(1L);
        toSave.setName("test");
        toSave.setPassword("password");
        authenticationRepository.persist(toSave);
        Apiuser apiuser = authenticationRepository.fetchUser(1L);
        assertThat(apiuser).isNotNull();
    }


    @Test
    public void testSaveAndFetchEvent() {
        SensorEvent toSave = new SensorEvent();
//        toSave.setSensorID("test");
//        toSave.setValue("-19c");
        toSave.setRegTime(LocalDateTime.now());
        repo.persist(toSave);
        List<SensorEvent> sensorEvents = repo.fetchAllEvents();
        assertThat(sensorEvents).hasSize(1);
    }

    @Test
    public void testOnlyFetchEventsAfterDate() {
        Sensor s = new Sensor();
        s.setSensorId("test");
        SensorEvent e1 = new SensorEvent();
        e1.setUpdated(LocalDateTime.now().minusDays(0));
        SensorEvent e2 = new SensorEvent();
        e2.setUpdated(LocalDateTime.now().minusDays(2));
        s.setEvents(Arrays.asList(e1, e2));

        Sensor s2 = new Sensor();
        s.setSensorId("test2");
        SensorEvent e3 = new SensorEvent();
        e3.setUpdated(LocalDateTime.now().minusDays(0));
        s2.setEvents(Arrays.asList(e3));

        repo.persist(s);
        repo.persist(s2);

        List<Sensor> sensors = repo.fetchAllSensors(null);
        assertThat(sensors.get(0).getSensorId()).isEqualTo(s.getSensorId());

        List<SensorEvent> events = repo.fetchEventsForSensorUpdatedAfter(sensors.get(0).getId(), LocalDateTime.now().minusDays(1));
        assertThat(events).hasSize(1);
    }


}

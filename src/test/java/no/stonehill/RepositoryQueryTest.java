package no.stonehill;

import no.stonehill.domain.Apiuser;
import no.stonehill.domain.SensorEvent;
import no.stonehill.persistence.AuthenticationRepository;
import no.stonehill.persistence.SensorRepository;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
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


}

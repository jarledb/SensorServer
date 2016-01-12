package no.stonehill;

import no.stonehill.domain.Apiuser;
import no.stonehill.domain.SensorEvent;
import no.stonehill.persistence.AuthenticationRepository;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class RepositoryQueryTest extends AbstractDbTest {
    AuthenticationRepository repo;

    public static final String TEST_VALUE = "testValue";
    public static final String TESTKEY = "testkey";
    public static final String TEST_VALUE_2 = "testValue2";

    @Before
    public void init() throws NoSuchFieldException, IllegalAccessException {
        Field f = AuthenticationRepository.class.getDeclaredField("em");
        repo = new AuthenticationRepository();
        f.setAccessible(true);
        f.set(repo, em);
    }

    @Test
    public void testSaveAndFetchUser() {
        Apiuser toSave = new Apiuser();
        toSave.setId(1L);
        toSave.setName("test");
        toSave.setPassword("password");
        repo.persist(toSave);
        Apiuser apiuser = repo.fetchUser(1L);
        assertThat(apiuser).isNotNull();
    }


    @Test
    public void testSaveAndFetchEvent() {
        SensorEvent toSave = new SensorEvent();
        toSave.setSensorID("test");
        toSave.setValue("-19c");
        toSave.setRegTime(LocalDateTime.now());
        repo.persist(toSave);
        List<SensorEvent> sensorEvents = repo.fetchAllEvents();
        assertThat(sensorEvents).hasSize(1);
    }


}

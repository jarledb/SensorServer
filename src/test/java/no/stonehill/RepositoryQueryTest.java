package no.stonehill;

import no.stonehill.domain.Apiuser;
import no.stonehill.persistence.AuthenticationRepository;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

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
    public void fetchOrderDatesJQLShouldCompile() {
        Apiuser toSave = new Apiuser();
        toSave.setId(1L);
        toSave.setName("test");
        toSave.setPassword("password");
        repo.persist(toSave);
        Apiuser apiuser = repo.fetchUser(1L);
        assertThat(apiuser).isNotNull();
    }


}

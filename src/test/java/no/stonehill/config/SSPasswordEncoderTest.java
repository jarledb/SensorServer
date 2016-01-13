package no.stonehill.config;

import org.fest.assertions.Assertions;
import org.junit.Test;

public class SSPasswordEncoderTest {
    private static String PASSWORD = "admin";
    private static String ENCODED_PASSWORD = "$6$24g1VtTb$AtqUBDJikMb0n/EADU64RFHogMogTzdHScfFxmuEMobDrfkgT91oGpoolEJEE6aRj516hB1rtwAIfXi7xEqNR/";
    private static SSPasswordEncoder encoder = new SSPasswordEncoder();

    @Test
    public void printEncodedPassword() throws Exception {
        System.out.println(encoder.encode(PASSWORD));
    }

    @Test
    public void testMatches() throws Exception {
        Assertions.assertThat(encoder.matches(PASSWORD, ENCODED_PASSWORD)).isTrue();
    }
}
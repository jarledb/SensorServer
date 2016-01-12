package no.stonehill.config;

import org.fest.assertions.Assertions;
import org.junit.Test;

public class SSPasswordEncoderTest {
    private static String PASSWORD = "admin";
    private static String ENCODED_PASSWORD = "$6$FHaS8Bna$v5b2P.p5ypWoxgCDVCivVlDCFYG1V0FgraI4aAlIEaXU.7PKal0eKMKp2U4e/Qf7QV/5EPN2GOJkbqll01su2/";
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
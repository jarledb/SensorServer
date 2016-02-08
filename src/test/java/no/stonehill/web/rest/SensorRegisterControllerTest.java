package no.stonehill.web.rest;

import junit.framework.TestCase;
import no.stonehill.domain.EventValue;
import no.stonehill.domain.SensorEvent;
import org.fest.assertions.Assertions;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jarle on 08.02.16.
 */
public class SensorRegisterControllerTest extends TestCase {

    @Test
    public void testIdenticalEvents() throws Exception {
        SensorEvent event = getSensorEvent(0, new EventValue("TEMP", "22"), new EventValue("HUMIDITY", "50"));
        List<SensorEvent> oldEvents = Arrays.asList(
                getSensorEvent(60, new EventValue("TEMP", "22"), new EventValue("HUMIDITY", "50")),
                getSensorEvent(120, new EventValue("TEMP", "22"), new EventValue("HUMIDITY", "50"))
        );
        Assertions.assertThat(SensorRegisterController.isIdentical(event, oldEvents)).isTrue();
    }

    @Test
    public void testNonIdenticalEvents() throws Exception {
        SensorEvent event = getSensorEvent(0, new EventValue("TEMP", "22"), new EventValue("HUMIDITY", "50"));
        List<SensorEvent> oldEvents = Arrays.asList(
                getSensorEvent(60, new EventValue("TEMP", "22"), new EventValue("HUMIDITY", "50")),
                getSensorEvent(120, new EventValue("TEMP", "44"), new EventValue("HUMIDITY", "60"))
        );
        Assertions.assertThat(SensorRegisterController.isIdentical(event, oldEvents)).isFalse();
    }

    private SensorEvent getSensorEvent(int secondsAgo, EventValue... eventValue) {
        SensorEvent event = new SensorEvent();
        event.setRegTime(LocalDateTime.now().minusSeconds(secondsAgo));
        Arrays.asList(eventValue).forEach(e -> event.addEventValue(e));
        return event;
    }
}
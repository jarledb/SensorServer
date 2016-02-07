package no.stonehill.web.rest;

import no.stonehill.domain.EventValue;
import no.stonehill.domain.Sensor;
import no.stonehill.domain.SensorEvent;
import no.stonehill.persistence.SensorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@RestController
public class SensorRegisterController {
    private static Logger LOG = LoggerFactory.getLogger(SensorRegisterController.class);

    @Autowired
    SensorRepository sensorRepository;

    @Transactional
    @RequestMapping(value = "/register/sensor", method = RequestMethod.POST)
    public Serializable registerSensor(@RequestParam(value = "id") String sensorId,
                                       @RequestParam String name,
                                       @RequestParam String type
    ) {
        Sensor sensor = new Sensor();
        sensor.setName(name);
        sensor.setSensorId(sensorId);
        sensor.setType(type);
        return sensorRepository.persist(sensor);
    }

    @Transactional
    @RequestMapping(value = "/register/sensor/event", method = RequestMethod.POST)
    //TODO: Rewrite typevalue to body data
    public Serializable registerEvent(@RequestParam(value = "id") String id,
                                      @RequestBody List<EventValue> typevalue
    ) {
        Sensor sensor;
        try { //If id is a Long then its a DB id, if not it is the sensorID
            sensor = sensorRepository.fetchSensor(Long.parseLong(id));
        } catch (NumberFormatException e) {
            sensor = sensorRepository.fetchSensorBySensorId(id);
        }

        if (sensor == null) {
            //TODO: throw exception
            LOG.warn("Could not find sensor with ID: " + id);
            return "Could not find sensor";
        }
        SensorEvent event = new SensorEvent();
        event.setRegTime(LocalDateTime.now());
        event.setUpdated(LocalDateTime.now());
        event.setIdenticalEvents(0);
        for (EventValue eventValue : typevalue) {
            if (eventValue != null && eventValue.isValid()) {
                event.addEventValue(eventValue);
            }
        }

//        List<SensorEvent> lastTwoEvents = sensorRepository.getLastEventForSensor(sensor);
//
//
//        boolean identical = isIdentical(event, lastTwoEvents.get(1));
//        identical = identical && isIdentical(event, lastTwoEvents.get(0));
//        if (identical) {
//            event = lastTwoEvents.get(0);
//            event.incrementIdenticalEvents();
//            event.setUpdated(LocalDateTime.now());
//            LOG.info("Updating: " + sensor.getName() + " " + event.toString());
//        }
        LOG.info("Updating: " + sensor.getName() + " " + event.toString());

        event.setSensor(sensor);
        sensor.addValue(event);
        return event;
    }

    private boolean isIdentical(SensorEvent newEvent, SensorEvent lastEvent) {
        LocalDateTime someTimeAgo = LocalDateTime.now().minusMinutes(60);

        if (lastEvent != null && lastEvent.getRegTime() != null && lastEvent.getRegTime().isAfter(someTimeAgo)) {
            boolean identical = true;
            for (EventValue eventValue : newEvent.getValues()) {
                if (!lastEvent.getValues().contains(eventValue)) {
                    identical = false;
                }
            }
            return identical;
        } else {
            return false;
        }

    }


    @RequestMapping(value = "/register/sensor", method = RequestMethod.GET)
    public List<SensorEvent> getAllReadings() {
        return sensorRepository.fetchAllEvents();
    }
}

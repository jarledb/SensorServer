package no.stonehill.web.rest;

import no.stonehill.domain.EventValue;
import no.stonehill.domain.Sensor;
import no.stonehill.domain.SensorEvent;
import no.stonehill.persistence.SensorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.time.LocalDateTime;
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
                                      @RequestParam(value = "typevalue") List<List<String>> typevalue
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
        for (List<String> strings : typevalue) {
            if (strings != null && strings.size() == 2) {
                event.addEventValue(new EventValue(strings.get(0), strings.get(1)));
            }
        }
        event.setSensor(sensor);
        event.setRegTime(LocalDateTime.now());
        sensor.addValue(event);
        LOG.info("Registering: " + sensor.getName() + " " + event.toString());

        return event;
    }


    @RequestMapping(value = "/register/sensor", method = RequestMethod.GET)
    public List<SensorEvent> getAllReadings() {
        return sensorRepository.fetchAllEvents();
    }
}

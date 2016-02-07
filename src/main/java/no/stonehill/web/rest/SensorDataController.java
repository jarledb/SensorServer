package no.stonehill.web.rest;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import no.stonehill.domain.Sensor;
import no.stonehill.domain.SensorEvent;
import no.stonehill.persistence.SensorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class SensorDataController {
    private static Logger LOG = LoggerFactory.getLogger(SensorDataController.class);

    @Autowired
    SensorRepository sensorRepository;

    @RequestMapping(value = "data/templog/{id}", method = RequestMethod.GET)
    public Serializable getSensorLogData(@PathVariable(value = "id") String id, @RequestParam(value = "days", defaultValue = "1") int daysInPast) {
        Sensor sensor;
        try { //If id is a Long then its a DB id, if not it is the sensorID
            sensor = sensorRepository.fetchSensor(Long.parseLong(id));
        } catch (NumberFormatException e) {
            sensor = sensorRepository.fetchSensorBySensorId(id);
        }
        sensor.setEvents(sensor.getEvents()
                .stream().filter(
                        event -> event.getUpdated().isAfter(LocalDateTime.now().minusDays(daysInPast))
                ).collect(Collectors.toSet()));
        sensor.sortEventsByDate();
        return sensor;
    }

    @JsonView(Views.Summary.class)
    @RequestMapping(value = "data/templog", method = RequestMethod.GET)
    public List<Sensor> getAllTempSensors(@RequestParam(defaultValue = "") String type) {
        List<Sensor> sensors = sensorRepository.fetchAllSensors(type);
        sensors.sort(Sensor.SENSOR_NAME_COMPARATOR);
        return sensors;
    }
}

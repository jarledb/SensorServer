package no.stonehill.web.rest;

import com.fasterxml.jackson.annotation.JsonView;
import no.stonehill.domain.Sensor;
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
import java.util.List;

@RestController
@RequestMapping
public class SensorDataController {
    private static Logger LOG = LoggerFactory.getLogger(SensorDataController.class);

    @Autowired
    SensorRepository sensorRepository;

    @RequestMapping(value = "data/templog/{id}", method = RequestMethod.GET)
    public Serializable getSensorLogData(@PathVariable(value = "id") String id) {
        Sensor sensor = sensorRepository.fetchSensor(Long.parseLong(id));

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

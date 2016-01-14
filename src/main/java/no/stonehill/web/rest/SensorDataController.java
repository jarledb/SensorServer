package no.stonehill.web.rest;

import no.stonehill.domain.TempSensor;
import no.stonehill.persistence.AuthenticationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping
public class SensorDataController {
    private static Logger LOG = LoggerFactory.getLogger(SensorDataController.class);

    @Autowired
    AuthenticationRepository authenticationRepository;

    @RequestMapping(value = "data/templog/{id}", method = RequestMethod.GET)
    public Serializable getSensorLogData(@PathVariable(value = "id") String id) {
        TempSensor tempSensor = new TempSensor();
        tempSensor.setId(Long.parseLong(id));
        tempSensor.setSensorId("OregonTemp01");
        tempSensor.setName("Ute");
        for (int i = 0; i < 24; i++) {
            tempSensor.addValue(new Serializable[]{i, new Double(Math.random() * 20 - 15)});
        }
        return tempSensor;
    }

    @RequestMapping(value = "data/templog", method = RequestMethod.GET)
    public List<TempSensor> getAllTempSensors() {
        List<TempSensor> tempSensors = new ArrayList<>();
        TempSensor t1 = new TempSensor();
        t1.setId(1);
        t1.setSensorId("OregonTemp01");
        t1.setName("Ute");
        TempSensor t2 = new TempSensor();
        t2.setId(2);
        t2.setSensorId("OregonTemp02");
        t2.setName("Stue");
        TempSensor t3 = new TempSensor();
        t3.setId(3);
        t3.setSensorId("OregonTemp03");
        t3.setName("Kjøkken");
        TempSensor t4 = new TempSensor();
        t4.setId(4);
        t4.setSensorId("OregonTemp04");
        t4.setName("Soverom");
        tempSensors.add(t1);
        tempSensors.add(t2);
        tempSensors.add(t3);
        tempSensors.add(t4);
        return tempSensors;
    }
}

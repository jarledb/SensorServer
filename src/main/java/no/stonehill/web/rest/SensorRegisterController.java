package no.stonehill.web.rest;

import no.stonehill.domain.SensorEvent;
import no.stonehill.persistence.AuthenticationRepository;
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
    AuthenticationRepository authenticationRepository;

    @Transactional
    @RequestMapping(value = "/register/sensor", method = RequestMethod.POST)
    public Serializable register(@RequestParam(value = "id") String id,
                                 @RequestParam String value,
                                 @RequestParam String type
    ) {
        SensorEvent event = new SensorEvent();
        event.setSensorID(id);
        event.setValue(value);
        event.setSensorType(type);
        event.setRegTime(LocalDateTime.now());
        return authenticationRepository.persist(event);
    }

    @RequestMapping(value = "/register/sensor", method = RequestMethod.GET)
    public List<SensorEvent> getAllReadings() {
        return authenticationRepository.fetchAllEvents();
    }
}

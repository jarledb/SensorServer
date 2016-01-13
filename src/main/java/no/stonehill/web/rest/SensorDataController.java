package no.stonehill.web.rest;

import no.stonehill.persistence.AuthenticationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

        @RequestMapping(value = "data/templog", method = RequestMethod.GET)
        public Serializable register(@RequestParam(value = "id") String id) {
            List<Serializable[]> array = new ArrayList<>();
//            array.add(new String[]{"Time", "Temp"});
            for (int i = 0; i < 24; i++) {
                array.add(new Serializable[]{i, new Double(Math.random()*20-15)});
            }

            return array.toArray();
        }
}

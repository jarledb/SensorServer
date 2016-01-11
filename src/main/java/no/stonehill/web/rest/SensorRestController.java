package no.stonehill.web.rest;

import no.stonehill.domain.Apiuser;
import no.stonehill.persistence.AuthenticationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.io.Serializable;

@RestController
public class SensorRestController {
    private static Logger LOG = LoggerFactory.getLogger(SensorRestController.class);

    @Autowired
    AuthenticationRepository authenticationRepository;

    @Transactional
    @RequestMapping(value = "sensor", method = RequestMethod.GET)
    public Serializable register(@RequestParam(value = "id") String id,
                                 @RequestParam String value,
                                 @RequestParam String type
    ) {
        Apiuser toSave = new Apiuser();
        toSave.setId(1L);
        toSave.setName("test");
        toSave.setPassword("password123");
        LOG.error("Persisting");
        authenticationRepository.persist(toSave);
        LOG.info(":" + authenticationRepository.fetchUser(1L));
//        Apiuser apiuser = authenticationRepository.fetchUser(1L);
        //authenticationRepository.fetchUser(1L);
        return authenticationRepository.fetchUser(1L);
    }
}

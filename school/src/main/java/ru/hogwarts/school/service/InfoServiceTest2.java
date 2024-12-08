package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("test2")
public class InfoServiceTest2 implements InfoService {

    Logger logger = LoggerFactory.getLogger(InfoServiceImpl.class);

    public String getPort() {
        String port = "4321";
        logger.info("Info get port method is invoked");

        return port;
    }
}

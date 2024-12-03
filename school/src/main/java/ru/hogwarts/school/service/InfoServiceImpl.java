package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("production")
public class InfoServiceImpl implements InfoService {

    @Value("${server.port}")
    private String port;

    Logger logger = LoggerFactory.getLogger(InfoServiceImpl.class);

    public String getPort() {
        logger.info("Info get port method is invoked");

        return port;
    }
}

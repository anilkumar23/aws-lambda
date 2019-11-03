package com.javaworld.awslambda.smarttrack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Created by anil.saladi on 9/28/2019.
 */
@SpringBootApplication
public class SmartTrackApplication extends SpringBootServletInitializer {
    private static final Logger logger = LogManager.getLogger(SmartTrackApplication.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(SmartTrackApplication.class, args);
    }
}

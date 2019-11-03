package com.javaworld.awslambda.smarttrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Created by anil.saladi on 9/28/2019.
 */
@SpringBootApplication
public class SmartTrackApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(SmartTrackApplication.class, args);
    }
}

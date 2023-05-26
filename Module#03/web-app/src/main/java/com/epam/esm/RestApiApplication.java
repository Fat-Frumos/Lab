package com.epam.esm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main class for starting the REST API application.
 */
@SpringBootApplication
public class RestApiApplication {
    /**
     * The entry point of the application.
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(RestApiApplication.class, args);
    }
}

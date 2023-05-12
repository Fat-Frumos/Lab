package com.epam.esm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.epam.esm"})
public class RestApiApplication  {

    public static void main(String[] args) {
        SpringApplication.run(RestApiApplication.class, args);
    }
}

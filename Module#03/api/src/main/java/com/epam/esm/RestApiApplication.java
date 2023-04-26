package com.epam.esm;

import com.epam.esm.config.PersistenceConfig;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;

@NoArgsConstructor
@SpringBootApplication
@EntityScan(basePackages = "com.epam.esm.model.entity")
@Import(PersistenceConfig.class)
public class RestApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestApiApplication.class, args);
    }
}

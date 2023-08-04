package com.epam.esm;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest(args = "--test-arg")
class RestApiApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Environment environment;


    @Test
    void testArgShouldBePresentInEnvironment() {
        Assertions.assertTrue(environment.containsProperty("test-arg"));
    }

    @Test
    void contextLoads() {
        Assertions.assertNotNull(applicationContext);
    }

    @Test
    void configureShouldSetSources() {
        RestApiApplication application = new RestApiApplication();
        SpringApplicationBuilder builder = mock(SpringApplicationBuilder.class);
        application.configure(builder);
        verify(builder).sources(RestApiApplication.class);
    }
}

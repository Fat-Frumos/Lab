package com.epam.esm;

import com.epam.esm.config.AppConfig;
import com.epam.esm.config.PostgreSQLConfig;
import com.epam.esm.config.RestConfig;
import com.epam.esm.handler.ErrorHandlerController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = PostgreSQLConfig.class)
@ComponentScan("com.epam.esm")
@PropertySource(value = {"classpath:application-dev.properties"}, ignoreResourceNotFound = true)
@ActiveProfiles("dev")
class ConfigTest {
    @Mock
    private DataSource dataSource;
    @Mock
    private DataSource dataSourceDev;
    @Mock
    private JdbcTemplate jdbcTemplateDev;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @InjectMocks
    AppConfig appConfig;

    @Mock
    private HttpHeaders httpHeaders;
    @Mock
    private ErrorHandlerController errorHandlerController;

    @Test
    @DisplayName("Should load jdbcTemplate bean")
    void shouldLoadJdbcTemplateBean() {
        assertNotNull(jdbcTemplate);
    }

    @Test
    @DisplayName("Check that dataSourceDev bean is created")
    void testDataSourceDev() {
        assertNotNull(dataSourceDev);
    }

    @Test
    @DisplayName("Check that jdbcTemplateDev bean is created")
    void testJdbcTemplateDev() {
        assertNotNull(jdbcTemplateDev);
    }

    @Test
    @DisplayName("Data source should be created")
    void dataSourceShouldBeCreated() {
        assertNotNull(dataSource);
    }

    @Test
    @DisplayName("Jdbc template should be created")
    void jdbcTemplateShouldBeCreated() {
        assertNotNull(jdbcTemplate);
    }

    @Test
    @DisplayName("HTTP headers should be created")
    void httpHeadersShouldBeCreated() {
        assertNotNull(httpHeaders);
    }

    @Test
    @DisplayName("Transaction manager should be created")
    void transactionManagerShouldBeCreated() {
        assertNotNull(appConfig.transactionManager());
    }

    @Test
    @DisplayName("Annotation driven transaction manager should be created")
    void annotationDrivenTransactionManagerShouldBeCreated() {
        assertNotNull(appConfig.annotationDrivenTransactionManager());
    }

    @Test
    @DisplayName("Error handler controller should be created")
    void errorHandlerControllerShouldBeCreated() {
        assertNotNull(errorHandlerController);
    }

    @Test
    @DisplayName("configureContentNegotiation() should set default content type to MediaType.APPLICATION_JSON")
    void configureContentNegotiationShouldSetDefaultContentType() {
        ContentNegotiationConfigurer configurer = mock(ContentNegotiationConfigurer.class);
        RestConfig restConfig = new RestConfig();
        restConfig.configureContentNegotiation(configurer);
        verify(configurer).defaultContentType(APPLICATION_JSON);
    }

    @Test
    @DisplayName("Should create transaction manager bean")
    void shouldCreateTransactionManager() {
        PlatformTransactionManager actual = appConfig.transactionManager();
        assertNotNull(actual);
        assertTrue(actual instanceof DataSourceTransactionManager);
        DataSourceTransactionManager transactionManager =
                (DataSourceTransactionManager) actual;
        assertSame(appConfig.dataSource(),
                transactionManager.getDataSource());
    }

    @Test
    @DisplayName("AppConfig class tests")
    void testAnnotationDrivenTransactionManager() {
        PlatformTransactionManager actual = appConfig.annotationDrivenTransactionManager();
        assertNotNull(actual);
        assertEquals("DataSourceTransactionManager",
                actual.getClass().getSimpleName());
    }

    @Test
    @DisplayName("Should create error handler controller bean")
    void shouldCreateErrorHandlerController() {
        assertNotNull(appConfig.errorHandlerController());
    }
    @Test
    void shouldCreateDataSource() {
        DataSource dataSource = appConfig.dataSource();
        assertNotNull(dataSource);
        assertSame(dataSource, appConfig.dataSource());
    }
}

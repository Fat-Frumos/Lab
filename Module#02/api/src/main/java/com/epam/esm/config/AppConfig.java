package com.epam.esm.config;

import com.epam.esm.handler.ErrorHandlerController;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;

/**
 * Configures the application's web environment.
 * Enables web MVC, transaction management,
 * and component scanning for the specified base package.
 * <p>
 * Provides configuration for the in-memory H2 database
 * and sets up a {@link JdbcTemplate} for interacting with the database.
 * <p>
 * Provides configuration for HTTP headers and an error handler controller.
 */
@EnableWebMvc
@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
@ComponentScan("com.epam.esm")
@PropertySource(
        value = {"classpath:application.properties"},
        ignoreResourceNotFound = true)
public class AppConfig implements TransactionManagementConfigurer {
    private DataSource dataSource;

    /**
     * Configures the in-memory H2 database
     * and returns the embedded database for the application.
     *
     * @return the configured embedded database
     */
    @Bean
    public DataSource dataSource() {
        if (dataSource == null) {
            dataSource = createDataSource();
        }
        return dataSource;
    }

    private DataSource createDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:db/schema.sql")
                .addScript("classpath:db/data.sql")
                .ignoreFailedDrops(true)
                .setName("db")
                .build();
    }

    /**
     * Configures a {@link JdbcTemplate} for interacting with the database.
     * Creates and returns a new JdbcTemplate instance.
     *
     * @return a new JdbcTemplate instance
     */
    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    /**
     * Creates and returns a new HttpHeaders instance.
     *
     * @return the configured HTTP headers
     */
    @Bean
    public HttpHeaders httpHeaders() {
        return new HttpHeaders();
    }

    /**
     * Configures a transaction manager
     * for managing transactions with the database.
     *
     * @return the configured transaction manager
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    /**
     * Configures the transaction manager to be used
     * for managing transactions with the database.
     *
     * @return the configured transaction manager
     */
    @NonNull
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return transactionManager();
    }

    /**
     * Creates and returns a new ErrorHandlerController instance.
     *
     * @return a new ErrorHandlerController instance
     */
    @Bean
    public ErrorHandlerController errorHandlerController() {
        return new ErrorHandlerController();
    }
}

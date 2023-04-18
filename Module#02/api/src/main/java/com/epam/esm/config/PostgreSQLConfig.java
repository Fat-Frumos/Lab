package com.epam.esm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * This class configures the PostgreSQL database connection
 * using Spring's DataSource and JdbcTemplate.
 * <p>
 * It also specifies the component scan and property source.
 */
@ComponentScan("com.epam.esm")
@PropertySource(value = {"classpath:application-dev.properties"}, ignoreResourceNotFound = true)
public class PostgreSQLConfig {

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.url}")
    private String url;

    /**
     * Creates a DataSource instance with the given
     * driver class name, url, username and password.
     * <p>
     * The {@code @Profile} annotation is used to indicate that a particular bean
     * or method should be used only when a certain profile is active.
     *
     * @return a new DataSource instance.
     */
    private DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    /**
     * Returns a DataSource instance for the "prod" profile.
     *
     * @return a DataSource instance for production environment.
     */
    @Profile("prod")
    public DataSource dataSourceProd() {
        return dataSource();
    }

    /**
     * Returns a JdbcTemplate instance for the "prod" profile.
     * Used only when the "prod" profile is active.
     *
     * @return a JdbcTemplate instance for production environment.
     */
    @Bean
    @Profile("prod")
    public JdbcTemplate jdbcTemplateProd() {
        return new JdbcTemplate(dataSourceProd());
    }

    /**
     * Returns a DataSource instance for the "dev" profile.
     *
     * @return a DataSource instance for development environment.
     */
    @Profile("dev")
    public DataSource dataSourceDev() {
        return dataSource();
    }

    /**
     * Returns a JdbcTemplate instance for the "dev" profile.
     * Used only when the "dev" profile is active.
     *
     * @return a JdbcTemplate instance for development environment.
     */
    @Bean
    @Profile("dev")
    public JdbcTemplate jdbcTemplateDev() {
        return new JdbcTemplate(dataSourceDev());
    }
}

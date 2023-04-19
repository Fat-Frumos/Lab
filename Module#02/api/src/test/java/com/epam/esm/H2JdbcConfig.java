package com.epam.esm;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Configuration class for setting up an H2 in-memory database.
 * <p>
 * Enables transaction management and sets up a data source and JdbcTemplate for use in tests.
 */
@Configuration
@EnableTransactionManagement
public class H2JdbcConfig {
    /**
     * Provides a data source for use in tests. Uses an embedded H2 in-memory database and runs
     * SQL scripts to set up schema, data, and create a user.
     *
     * @return the data source
     */
    @Profile("test")
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:db/schema.sql")
                .addScript("classpath:db/data.sql")
                .addScript("classpath:db/create-user.sql")
                .ignoreFailedDrops(true)
                .setScriptEncoding("UTF-8")
                .setName("db")
                .build();
    }

    /**
     * Provides a JdbcTemplate for use in tests.
     * Uses the data source created by {@link #dataSource()}.
     *
     * @return the JdbcTemplate
     */
    @Bean
    @Profile("test")
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
}

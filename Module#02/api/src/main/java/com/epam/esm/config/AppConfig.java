package com.epam.esm.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;

@EnableWebMvc
@Configuration
@AllArgsConstructor
@EnableTransactionManagement
@ComponentScan("com.epam.esm")
public class AppConfig implements TransactionManagementConfigurer {

    private static final String USERNAME = "reddit_5qs5_user";
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String PASSWORD = "9pnjM54Tkgtiqwdfq20yaSXN4YNNEBuQ";
    private static final String URL = "jdbc:postgresql://oregon-postgres.render.com:5432/reddit_5qs5";

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource =
                new DriverManagerDataSource();
        dataSource.setDriverClassName(DRIVER);
        dataSource.setUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager txManager() {

        return new DataSourceTransactionManager(dataSource());
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {

        return txManager();
    }

    @Bean
    public JdbcTemplate getJdbcTemplate() {

        return new JdbcTemplate(dataSource());
    }
}

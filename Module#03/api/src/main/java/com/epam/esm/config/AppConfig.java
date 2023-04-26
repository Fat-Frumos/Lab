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

        @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

        @Bean
    public HttpHeaders httpHeaders() {
        return new HttpHeaders();
    }

        @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

        @NonNull
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return transactionManager();
    }

        @Bean
    public ErrorHandlerController errorHandlerController() {
        return new ErrorHandlerController();
    }
}

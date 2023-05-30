package com.epam.esm.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.Properties;

/**
 * Configuration class for persistence-related beans.
 */
@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
public class PersistenceConfig {
    /**
     * The property for showing SQL statements.
     */
    @Value("${spring.jpa.show-sql}")
    private String showSql;

    /**
     * The database URL.
     */
    @Value("${spring.datasource.url}")
    private String url;

    /**
     * The database username.
     */
    @Value("${spring.datasource.username}")
    private String username;

    /**
     * The database password.
     */
    @Value("${spring.datasource.password}")
    private String password;

    /**
     * The Hibernate DDL auto property.
     */
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String hibernateDdlAuto;

    /**
     * The database driver class name.
     */
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    /**
     * The Hibernate dialect property.
     */
    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String dialect;

    /**
     * Creates an {@link EntityManager} bean.
     *
     * @param factoryBean the {@link LocalContainerEntityManagerFactoryBean} bean.
     * @return the created {@link EntityManager} bean.
     * @throws IllegalStateException if the entity manager factory
     *                               is not initialized.
     */
    @Bean
    public EntityManager entityManager(
            final LocalContainerEntityManagerFactoryBean factoryBean) {
        return Optional.ofNullable(factoryBean.getObject())
                .map(EntityManagerFactory::createEntityManager)
                .orElseThrow(() -> new IllegalStateException(
                        "Entity manager factory is not initialized"));
    }

    /**
     * Creates a {@link DataSourceInitializer} bean for executing database scripts.
     *
     * @param dataSource the {@link DataSource} bean.
     * @return the created {@link DataSourceInitializer} bean.
     */
    @Bean
    public DataSourceInitializer dataSourceScriptInitializer(
            final DataSource dataSource) {
        ResourceDatabasePopulator resourceDatabasePopulator =
                new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(new ClassPathResource("db/schema.sql"));
        resourceDatabasePopulator.addScript(new ClassPathResource("db/gift.sql"));
        resourceDatabasePopulator.addScript(new ClassPathResource("db/tags.sql"));
        resourceDatabasePopulator.addScript(new ClassPathResource("db/user.sql"));
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(dataSource);
        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
        return dataSourceInitializer;
    }

    /**
     * Creates a {@link DataSource} bean.
     *
     * @return the created {@link DataSource} bean.
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource =
                new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    /**
     * Creates a {@link LocalContainerEntityManagerFactoryBean}
     * bean for managing the entity manager factory.
     *
     * @param dataSource the {@link DataSource} bean.
     * @return the created {@link LocalContainerEntityManagerFactoryBean} bean.
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            final DataSource dataSource) {
        Properties properties = new Properties();
        properties.setProperty("hibernate.show_sql", showSql);
        properties.setProperty("hibernate.hbm2ddl.auto", hibernateDdlAuto);
        properties.setProperty("hibernate.dialect", dialect);
        properties.setProperty("hibernate.default_batch_fetch_size", "16");
        LocalContainerEntityManagerFactoryBean factoryBean =
                new LocalContainerEntityManagerFactoryBean();
        factoryBean.setJpaProperties(properties);
        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan("com.epam.esm.entity");
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return factoryBean;
    }

    /**
     * Creates a {@link PlatformTransactionManager} bean for managing transactions.
     *
     * @param factoryBean the {@link LocalContainerEntityManagerFactoryBean} bean.
     * @return the created {@link PlatformTransactionManager} bean.
     */
    @Bean
    public PlatformTransactionManager transactionManager(
            final LocalContainerEntityManagerFactoryBean factoryBean) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(factoryBean.getObject());
        return transactionManager;
    }
}

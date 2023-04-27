package com.epam.esm;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
class CriteriaTest {

    @BeforeEach
    void setUp() {
        EmbeddedDatabase dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:db/schema.sql")
                .addScript("classpath:db/data.sql")
                .ignoreFailedDrops(true)
                .setName("db")
                .build();
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan("com.epam.esm.entity");
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    }
}

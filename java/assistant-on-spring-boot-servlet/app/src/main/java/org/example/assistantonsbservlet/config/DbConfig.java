package org.example.assistantonsbservlet.config;

import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
class DbConfig {
    @Bean
    DataSource pubChemDataSource(AppProperties properties) {
        final var pubChemProps = properties.pubChem();
        final var ds = pubChemProps.ds();
        final var hikari = pubChemProps.hikari();
        return createHikariDataSource(ds, hikari);
    }

    private static HikariDataSource createHikariDataSource(DataSourceProperties ds, HikariProperties hikari) {
        final var dataSource = DataSourceBuilder.create()
            .type(HikariDataSource.class)
            .driverClassName(ds.getDriverClassName())
            .url(ds.getUrl())
            .username(ds.getUsername())
            .password(ds.getPassword())
            .build();

        dataSource.setPoolName(hikari.poolName());
        dataSource.setMaximumPoolSize(hikari.maximumPoolSize());
        dataSource.setMinimumIdle(hikari.minimumIdle());
        dataSource.setConnectionTimeout(hikari.connectionTimeout().toMillis());
        dataSource.setValidationTimeout(hikari.validationTimeout().toMillis());
        dataSource.setIdleTimeout(hikari.idleTimeout().toMillis());
        dataSource.setMaxLifetime(hikari.maxLifetime().toMillis());
        dataSource.setKeepaliveTime(hikari.keepAliveTime().toMillis());
        return dataSource;
    }

    @Bean
    DataSource stockMarketDataSource(AppProperties properties) {
        final var stockedMarketProps = properties.stockMarket();
        final var ds = stockedMarketProps.ds();
        final var hikari = stockedMarketProps.hikari();
        return createHikariDataSource(ds, hikari);
    }

    @Bean
    SpringLiquibase pubChemLiquibase(@Qualifier("pubChemDataSource") DataSource dataSource) {
        final var liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:db/changelog/db.pubchem-food-additive-changelog-master.xml");
        liquibase.setContexts("pubchem");
        liquibase.setShouldRun(true);
        return liquibase;
    }

    @Bean
    LocalContainerEntityManagerFactoryBean pubChemEntityManagerFactory(
        @Qualifier("pubChemDataSource") DataSource ds
    ) {
        final var factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPersistenceUnitName("pubchem-unit");
        factoryBean.setPackagesToScan("org.example.db.pubchem.fda.model");
        factoryBean.setDataSource(ds);
        final var jpaVendorAdapter = newHibernateJpaVendorAdapter();
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        factoryBean.setJpaPropertyMap(jpaPropertyMap());
        return factoryBean;
    }

    private static Map<String, Object> jpaPropertyMap() {
        final var jpaPropertyMap = new HashMap<String, Object>();
        jpaPropertyMap.put("hibernate.format_sql", true);
        jpaPropertyMap.put("hibernate.highlight_sql", true);
        jpaPropertyMap.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        return jpaPropertyMap;
    }

    private static HibernateJpaVendorAdapter newHibernateJpaVendorAdapter() {
        final var jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setShowSql(true);
        return jpaVendorAdapter;
    }

    @Bean
    LocalContainerEntityManagerFactoryBean stockMarketEntityManagerFactory(
        @Qualifier("stockMarketDataSource") DataSource ds
    ) {
        final var factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPersistenceUnitName("stock-market-unit");
        factoryBean.setPackagesToScan("org.example.db.stockmarket");
        factoryBean.setDataSource(ds);
        final var jpaVendorAdapter = newHibernateJpaVendorAdapter();
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        factoryBean.setJpaPropertyMap(jpaPropertyMap());
        return factoryBean;
    }

    @Bean
    DataSource pubChemGraphDataSource(AppProperties properties) {
        final var pubChemProps = properties.pubChem();
        final var ds = pubChemProps.pgAgeGraph();
        final var hikari = pubChemProps.hikariPgAgeGraph();
        return createHikariDataSource(ds, hikari);
    }

    @Bean
    LocalContainerEntityManagerFactoryBean pubChemGraphEntityManagerFactory(
        @Qualifier("pubChemGraphDataSource") DataSource ds
    ) {
        final var factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPersistenceUnitName("pubchem-graph-unit");
        factoryBean.setPackagesToScan("org.example.db.pubchem.graph");
        factoryBean.setDataSource(ds);
        final var jpaVendorAdapter = newHibernateJpaVendorAdapter();
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        factoryBean.setJpaPropertyMap(jpaPropertyMap());
        return factoryBean;
    }
}

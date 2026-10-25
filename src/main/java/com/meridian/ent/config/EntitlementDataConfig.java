package com.meridian.ent.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
public class EntitlementDataConfig {

    @Bean(destroyMethod = "close")
    public BasicDataSource dataSource(@Value("${ent.jdbc.driver}") String driver,
                                      @Value("${ent.jdbc.url}") String url,
                                      @Value("${ent.jdbc.user}") String user,
                                      @Value("${ent.jdbc.password}") String password) {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(user);
        ds.setPassword(password);
        ds.setMaxActive(8);
        ds.setDefaultAutoCommit(true);
        return ds;
    }

    // local only. DBA owns the real schema
    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
        DataSourceInitializer init = new DataSourceInitializer();
        init.setDataSource(dataSource);
        init.setDatabasePopulator(new ResourceDatabasePopulator(
            new ClassPathResource("sql/schema.sql"), new ClassPathResource("sql/seed.sql")));
        return init;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}

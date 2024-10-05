package org.example.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class BasicDataSourceConfiguration {
    @Bean
    @ConfigurationProperties("spring.datasource")
    public BasicDataSource dataSource() {
        return DataSourceBuilder.create().type(BasicDataSource.class).build();
    }
}
package org.prgrms.be.app.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties({TestDataSourceProperties.class})
public class TestConfig {
    @Bean
    DataSource dataSource(TestDataSourceProperties properties) {
        return DataSourceBuilder.create()
                .driverClassName(properties.getDriverClassName())
                .url(properties.getUrl())
                .username(properties.getUsername())
                .password(properties.getPassword())
                .build();
    }

    @Bean
    NamedParameterJdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}

package org.prgrms.be.app.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "spring.datasource")
public class TestDataSourceProperties {

    private String driverClassName;
    private String url;
    private String username;
    private String password;
}

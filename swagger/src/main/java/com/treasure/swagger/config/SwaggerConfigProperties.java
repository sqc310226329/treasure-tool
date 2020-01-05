package com.treasure.swagger.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "swagger")
@Data
public class SwaggerConfigProperties {

    private String applicationName;

    private String basePackage;

}

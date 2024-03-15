package com.nikita.textgenerator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(value = "spring.cloud")
public class ConfigFeign {
    @Value("${spring.sendTypeFeign}")
    private String feignProperties;
}
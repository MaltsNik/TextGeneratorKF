package com.nikita.textgenerator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@ConfigurationProperties(prefix = "spring.kafka")

public class ConfigKafka {
//    @Value("${spring.sendTypeKafka}")
//    private String kafkaProperties;
}
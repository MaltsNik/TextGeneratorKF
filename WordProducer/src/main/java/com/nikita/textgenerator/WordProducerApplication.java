package com.nikita.textgenerator;

import com.nikita.textgenerator.config.ConfigKafka;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties(ConfigKafka.class)
public class WordProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WordProducerApplication.class, args);
    }
}
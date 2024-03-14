package com.nikita.textgenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class WordConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WordConsumerApplication.class, args);
    }
}

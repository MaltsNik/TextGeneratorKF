package com.nikita.textgenerator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikita.textgenerator.dto.Word;
import com.nikita.textgenerator.service.WordGenerator;
import com.nikita.textgenerator.service.WordSendService;
import com.nikita.textgenerator.service.WordSender;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProducerConfiguration {
    private static final Logger log = LoggerFactory.getLogger(ProducerConfiguration.class);
    public final String topicName;
    public final String bootstrapAddress;

    public ProducerConfiguration(@Value("${spring.kafka.topic}") String topicName,
                                 @Value("${spring.kafka.producer.bootstrap-server}") String bootstrapAddress) {
        this.topicName = topicName;
        this.bootstrapAddress = bootstrapAddress;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return JacksonUtils.enhancedObjectMapper();
    }

    @Bean
    public ProducerFactory<String, Word> producerFactory(ObjectMapper objectMapper) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        var kafkaProducerFactory = new DefaultKafkaProducerFactory<String, Word>(props);
        kafkaProducerFactory.setValueSerializer(new JsonSerializer<>(objectMapper));
        return kafkaProducerFactory;
    }

    @Bean
    public KafkaTemplate<String, Word> kafkaTemplate(ProducerFactory<String, Word> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name(topicName).partitions(2).build();
    }

    @Bean
    public WordSender wordSender(NewTopic topic, KafkaTemplate<String, Word> kafkaTemplate) {
        return new WordSendService(
                kafkaTemplate,
                topic.name());
    }

    @Bean
    public WordGenerator wordGenerator(WordSender wordSender) {
        return new WordGenerator(wordSender);
    }
}
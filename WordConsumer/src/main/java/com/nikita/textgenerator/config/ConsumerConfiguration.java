package com.nikita.textgenerator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikita.textgenerator.repository.entity.Word;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConsumerConfiguration {
    private static final Logger log = LoggerFactory.getLogger(ConsumerConfiguration.class);
    public final String topicName;
    public final String bootstrapAddress;

    public ConsumerConfiguration(@Value("${spring.kafka.topic}") String topicName,
                                 @Value("${spring.kafka.consumer.bootstrap-server}") String bootstrapAddress) {
        this.topicName = topicName;
        this.bootstrapAddress = bootstrapAddress;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return JacksonUtils.enhancedObjectMapper();
    }

    @Bean
    public ConsumerFactory<String, Word> consumerFactory(ObjectMapper objectMapper) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group-2");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.nikita.textgenerator.repository.entity.Word");
        props.put(JsonDeserializer.TYPE_MAPPINGS, "com.nikita.textgenerator.repository.entity.Word:com.nikita.textgenerator.repository.entity.Word");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.nikita.textgenerator.repository.entity.Word");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 3);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,3_000);

        var kafkaConsumerFactory = new DefaultKafkaConsumerFactory<String, Word>(props);
        kafkaConsumerFactory.setValueDeserializer(new JsonDeserializer<>(objectMapper));
        return kafkaConsumerFactory;
    }

    @Bean()
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Word>> listenerContainerFactory(
            ConsumerFactory<String, Word> consumerFactory) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, Word>();
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(true);
        factory.setConcurrency(1);
        factory.getContainerProperties().setIdleBetweenPolls(1_000);
        factory.getContainerProperties().setPollTimeout(1_000);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        var executor = new SimpleAsyncTaskExecutor("kafka-consumer-");
        executor.setConcurrencyLimit(10);
        var listenerTaskExecutor = new ConcurrentTaskExecutor(executor);
        factory.getContainerProperties().setListenerTaskExecutor(listenerTaskExecutor);
        return factory;
    }

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name(topicName).partitions(2).replicas(1).build();
    }
}
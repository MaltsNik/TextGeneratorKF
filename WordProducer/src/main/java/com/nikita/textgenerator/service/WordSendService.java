package com.nikita.textgenerator.service;

import com.nikita.textgenerator.dto.Word;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.function.Consumer;

public class WordSendService implements WordSender {
    private static final Logger log = LoggerFactory.getLogger(WordSendService.class);
    private final KafkaTemplate<String, Word> kafkaTemplate;

    private final Consumer<Word> sendAsk;
    private final String topic;

    public WordSendService(KafkaTemplate<String, Word> kafkaTemplate, Consumer<Word> sendAsk, String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.sendAsk = sendAsk;
        this.topic = topic;
    }

    @Override
    public void send(Word word) {
        try {
            log.info("word:{}", word);
            kafkaTemplate.send(topic, word).whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("message id:{} was send, offset:{}", word,
                            result.getRecordMetadata().offset());
                    sendAsk.accept(word);
                } else {
                    log.error("message id:{} was not sent", word, ex);
                }
            });
        } catch (Exception ex) {
            log.error("send error, word:{} ", word, ex);
        }
    }
}
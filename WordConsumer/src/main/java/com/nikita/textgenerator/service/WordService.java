package com.nikita.textgenerator.service;

import com.nikita.textgenerator.repository.WordRepository;
import com.nikita.textgenerator.repository.entity.Word;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class WordService {
    private final WordRepository wordRepository;

    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    @Transactional
    @KafkaListener(topics = ("${spring.kafka.topic}"), groupId = ("${spring.kafka.consumer.group-id}"),
            containerFactory = "listenerContainerFactory")
    public Word saveWord(Word word) {
        word.setReceivedDate(LocalDateTime.now());
        return wordRepository.save(word);
    }
}
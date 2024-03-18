package com.nikita.textgenerator.service;

import com.nikita.textgenerator.repository.WordRepository;
import com.nikita.textgenerator.repository.entity.Word;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
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
    public Word saveWord(Word word, Acknowledgment ack) throws InterruptedException {
        word.setReceivedDate(LocalDateTime.now());
        System.out.println("Received word: " + word.getWord() + " at " + word.getReceivedDate());
        Thread.sleep(60_000);
        Word savedWord = wordRepository.save(word);
        ack.acknowledge();
        return savedWord;
    }
}
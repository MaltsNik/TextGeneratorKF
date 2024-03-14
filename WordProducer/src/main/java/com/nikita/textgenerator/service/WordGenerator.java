package com.nikita.textgenerator.service;

import com.nikita.textgenerator.dto.Word;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Random;

public class WordGenerator {
    private static final Logger log = LoggerFactory.getLogger(WordGenerator.class);
    private final WordSender wordClient;

    public WordGenerator(WordSender wordClient) {
        this.wordClient = wordClient;
    }

    @Scheduled(fixedDelay = 3000)
    public void generate() {
        Word word = textGenerator();
        log.info("word to send - " + word.toString());
        wordClient.send(textGenerator());
        log.info("generation started");
    }

    public Word textGenerator() {
        Random random = new Random();
        int length = random.nextInt(100 + 1);
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = (char) (random.nextInt(26) + 'a');
            word.append(c);
        }
        return new Word(word.toString());
    }
}
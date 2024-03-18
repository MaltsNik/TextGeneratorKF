package com.nikita.textgenerator.controller;

import com.nikita.textgenerator.repository.entity.Word;
import com.nikita.textgenerator.service.WordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/words")
public class WordController {
    private final WordService wordService;
    private static final Logger log = LoggerFactory.getLogger(WordController.class);

    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

//    @PostMapping
//    public ResponseEntity<Word> saveWord(@RequestBody Word word) throws InterruptedException {
//        log.info(word.toString());
//        return ResponseEntity.ok(wordService.saveWord(word));
//    }
}
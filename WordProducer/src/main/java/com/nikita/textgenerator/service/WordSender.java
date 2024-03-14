package com.nikita.textgenerator.service;

import com.nikita.textgenerator.dto.Word;

public interface WordSender {
    void send(Word word);
}
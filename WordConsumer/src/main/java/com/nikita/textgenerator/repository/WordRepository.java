package com.nikita.textgenerator.repository;

import com.nikita.textgenerator.repository.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<Word, Long> {
}
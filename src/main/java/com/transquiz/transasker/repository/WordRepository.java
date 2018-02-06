package com.transquiz.transasker.repository;

import com.transquiz.transasker.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordRepository extends JpaRepository<Word, Long> {
    List<Word> getWordsBySourceWordIgnoreCase(String word);

}

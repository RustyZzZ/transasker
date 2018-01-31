package com.transquiz.transasker.service;

import com.transquiz.transasker.Dto.WordDto;
import com.transquiz.transasker.model.Languages;

import java.util.List;

public interface WordService {
    List<WordDto> getWordTranslation(String word, Languages langFrom, Languages langTo) throws Exception;
}

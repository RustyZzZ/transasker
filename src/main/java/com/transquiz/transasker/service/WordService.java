package com.transquiz.transasker.service;

import com.transquiz.transasker.dto.WordDto;
import com.transquiz.transasker.model.Languages;

import java.util.List;

public interface WordService {
    List<WordDto> getWordTranslation(String word, Languages langFrom, Languages langTo) throws Exception;

    List<WordDto> getWordTranslation(String word, Languages langFrom, Languages langTo, String telegramName, int chatId) throws Exception;

    List<WordDto> getWordTranslationPublic(String text, Languages langFrom, Languages langTo) throws Exception;
}

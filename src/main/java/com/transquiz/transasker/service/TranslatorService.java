package com.transquiz.transasker.service;

import com.transquiz.transasker.model.Languages;
import com.transquiz.transasker.model.Word;

import java.util.List;

public interface TranslatorService {
    List<Word> callUrlAndParseResult(Languages langFrom, Languages langTo, String word) throws Exception;
}

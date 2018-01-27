package com.transquiz.transasker.service;

import com.transquiz.transasker.model.Word;

import java.util.List;

public interface TranslatorService {
    List<Word> callUrlAndParseResult(String langFrom, String langTo, String word) throws Exception;
}

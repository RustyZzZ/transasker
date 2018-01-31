package com.transquiz.transasker.controller;

import com.transquiz.transasker.Dto.WordDto;
import com.transquiz.transasker.model.Languages;
import com.transquiz.transasker.service.TranslatorService;
import com.transquiz.transasker.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class TranslateController {

    private final TranslatorService translatorServiceImpl;
    private final WordService wordService;

    @Autowired
    public TranslateController(TranslatorService translatorServiceImpl, WordService wordService) {
        this.translatorServiceImpl = translatorServiceImpl;
        this.wordService = wordService;
    }

    @GetMapping("/translate")
    public List<WordDto> translateTo(@RequestParam String word) {
        try {
            return wordService.getWordTranslation(word, Languages.EN, Languages.RU);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}

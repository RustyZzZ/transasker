package com.transquiz.transasker.controller;

import com.transquiz.transasker.model.Word;
import com.transquiz.transasker.service.TranslatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class TranslateController {

    private final TranslatorService translatorServiceImpl;

    private static final String langTo = "ru";
    private static final String langFrom = "en";

    @Autowired
    public TranslateController(TranslatorService translatorServiceImpl) {
        this.translatorServiceImpl = translatorServiceImpl;
    }

    @GetMapping("/translate")
    public List<Word> translateTo(@RequestParam String word) {
        try {
            return translatorServiceImpl.callUrlAndParseResult(langFrom, langTo, word);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}

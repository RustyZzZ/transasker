package com.transquiz.transasker.service

import com.transquiz.transasker.model.Word
import groovy.json.JsonSlurper
import org.springframework.stereotype.Component

@Component
class JsonWordParser {
    List<Word> parseJsonWithWordsToListOfWords(String json) {
        List listOfWords = new ArrayList()
        def wordsMap = new JsonSlurper().parseText(json)
        def alternativeTranslations = wordsMap["alternative_translations"]
        String srcWord = alternativeTranslations["src_phrase"][0]
        def listOfWord = alternativeTranslations[0]["alternative"]
        listOfWord.each {
            def word = Word.builder()
                    .sourceWord(srcWord)
                    .targetWord(it["word_postproc"] as String)
                    .build()
            listOfWords.add(word)
        }
        listOfWords
    }
}

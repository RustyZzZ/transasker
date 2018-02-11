package com.transquiz.transasker.service;

import com.transquiz.transasker.model.Word;

import java.util.Set;

public interface QuizerService {

    Set<Word> getQuizWordsForProfileByTelegramName(String telegramName);

    Word getRandomQuizWordForTelegramName(String telegramName);

    boolean processAnswer(String tgUsername, String previousAnswer);

    Word getCurrentWord(String tgUsername);
}

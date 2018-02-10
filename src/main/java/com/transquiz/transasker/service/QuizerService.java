package com.transquiz.transasker.service;

import com.transquiz.transasker.model.Word;

import java.util.Set;

public interface QuizerService {

    public Set<Word> getQuizWordsForProfileByTelegramName(String telegramName);

    public boolean isUsersAnswerCorrect(String source, String answer);

    public Word getRandomQuizWordForTelegramName(String telegramName);

}

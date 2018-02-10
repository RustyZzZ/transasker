package com.transquiz.transasker.service.impls;

import com.transquiz.transasker.model.Profile;
import com.transquiz.transasker.model.Word;
import com.transquiz.transasker.repository.WordRepository;
import com.transquiz.transasker.service.ProfileService;
import com.transquiz.transasker.service.QuizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;

@Service
public class QuizerServiceImpl implements QuizerService {
    @Autowired
    private ProfileService profileService;

    @Autowired
    private WordRepository wordRepository;

    public Set<Word> getQuizWordsForProfileByTelegramName(String telegramName) {
        Profile profile = profileService.getProfileByTelegramUsernameWithoutChatId(telegramName);
        return profile.getWords();

    }

    public boolean isUsersAnswerCorrect(String source, String answer) {
        return wordRepository.existsBySourceWordAndTargetWord(source, answer);
    }

    public Word getRandomQuizWordForTelegramName(String telegramName) {
        Set<Word> quizWords = getQuizWordsForProfileByTelegramName(telegramName);
        return new ArrayList<>(quizWords).get(0);
    }
}

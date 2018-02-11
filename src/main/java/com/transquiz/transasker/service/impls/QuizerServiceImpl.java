package com.transquiz.transasker.service.impls;

import com.transquiz.transasker.model.Profile;
import com.transquiz.transasker.model.Word;
import com.transquiz.transasker.repository.WordRepository;
import com.transquiz.transasker.service.ProfileService;
import com.transquiz.transasker.service.QuizerService;
import com.transquiz.transasker.util.ControllableCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class QuizerServiceImpl implements QuizerService {
    private final ProfileService profileService;
    private final WordRepository wordRepository;

    private ControllableCache<String, Set<Word>> cachedWords = new ControllableCache<>(this::getQuizWordsForProfileByTelegramName);
    private ConcurrentHashMap<String, Word> askedWord = new ConcurrentHashMap<>();

    @Autowired
    public QuizerServiceImpl(ProfileService profileService, WordRepository wordRepository) {
        this.profileService = profileService;
        this.wordRepository = wordRepository;
    }

    @Override
    public Set<Word> getQuizWordsForProfileByTelegramName(String telegramName) {
        Profile profile = profileService.getProfileByTelegramUsernameWithoutChatId(telegramName);
        return profile.getWords();
    }

    private boolean isUsersAnswerCorrect(String source, String answer) {
        return wordRepository.existsBySourceWordAndTargetWord(source, answer);
    }

    @Override
    public Word getRandomQuizWordForTelegramName(String telegramName) {
        Set<Word> quizWords = cachedWords.get(telegramName);
        int maxCountOfWords = quizWords.size();
        Word word = new ArrayList<>(quizWords).get(new Random().nextInt(maxCountOfWords));
        askedWord.put(telegramName, word);
        return word;
    }

    @Override
    public boolean processAnswer(String tgUsername, String answer) {
        Word currentWord = getCurrentWord(tgUsername);
        if (currentWord == null) {
            return false;
        }
        boolean isUsersAnswerCorrect = isUsersAnswerCorrect(currentWord.getSourceWord(), answer.toLowerCase());
        if (isUsersAnswerCorrect) {
            askedWord.remove(tgUsername);
            Set<Word> wordsOfUser = cachedWords.get(tgUsername);
            wordsOfUser.remove(currentWord);
            cachedWords.put(tgUsername, wordsOfUser);
        }
        return isUsersAnswerCorrect;
    }

    @Override
    public Word getCurrentWord(String tgUsername) {
        return askedWord.get(tgUsername);
    }
}

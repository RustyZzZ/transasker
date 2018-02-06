package com.transquiz.transasker.service.impls;

import com.transquiz.transasker.Dto.WordDto;
import com.transquiz.transasker.model.Languages;
import com.transquiz.transasker.model.Profile;
import com.transquiz.transasker.model.Word;
import com.transquiz.transasker.model.security.User;
import com.transquiz.transasker.repository.WordRepository;
import com.transquiz.transasker.service.ProfileService;
import com.transquiz.transasker.service.TranslatorService;
import com.transquiz.transasker.service.UserService;
import com.transquiz.transasker.service.WordService;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class WordServiceImpl implements WordService {
    private final WordRepository wordRepository;
    private final TranslatorService translatorService;
    private final ProfileService profileService;
    private final ModelMapper modelMapper = new ModelMapper();
    private final Type listType = new TypeToken<List<WordDto>>() {
    }.getType();

    private final UserService userService;

    @Autowired
    public WordServiceImpl(WordRepository wordRepository, TranslatorService translatorService, ProfileService profileService, UserService userService) {
        this.wordRepository = wordRepository;
        this.translatorService = translatorService;
        this.profileService = profileService;
        this.userService = userService;
    }

    @Transactional
    @Override
    public List<WordDto> getWordTranslation(String word, Languages langFrom, Languages langTo) throws Exception {
        User user = userService.getCurrentUser();
        Profile profileByUser = profileService.getProfileByUser(user);
        List<Word> resultWords = wordRepository.getWordsBySourceWordIgnoreCase(word);
        List<Word> wordsForDatabase = resultWords;
        if (CollectionUtils.isEmpty(resultWords)) {
            resultWords = translatorService.callUrlAndParseResult(langFrom, langTo, word.toLowerCase());
            wordsForDatabase = enhanceListOfWords(langFrom, langTo, resultWords);
        }
        profileService.addWordsToProfileAndUpdateProfile(profileByUser, new HashSet<>(wordsForDatabase));
        return modelMapper.map(resultWords, listType);
    }

    private List<Word> enhanceListOfWords(Languages langFrom, Languages langTo, List<Word> words) {
        List<Word> fixedWords = words.stream()
                .filter(w -> !StringUtils.equals(w.getSourceWord(), w.getTargetWord()))
                .peek(w -> {
                    w.setFromLang(langFrom);
                    w.setToLang(langTo);
                }).collect(Collectors.toList());
        return Stream.concat(fixedWords.stream(),
                fixedWords.stream().map(getReverseWords())).collect(Collectors.toList());
    }

    private Function<Word, Word> getReverseWords() {
        return w -> Word.builder()
                .sourceWord(w.getTargetWord())
                .targetWord(w.getSourceWord())
                .fromLang(w.getToLang())
                .toLang(w.getFromLang())
                .build();
    }

}

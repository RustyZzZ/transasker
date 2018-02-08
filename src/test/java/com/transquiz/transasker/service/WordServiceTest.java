package com.transquiz.transasker.service;

import com.google.common.collect.Sets;
import com.transquiz.transasker.dto.WordDto;
import com.transquiz.transasker.model.Languages;
import com.transquiz.transasker.model.Profile;
import com.transquiz.transasker.model.Word;
import com.transquiz.transasker.model.security.User;
import com.transquiz.transasker.repository.WordRepository;
import com.transquiz.transasker.service.impls.WordServiceImpl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import static com.transquiz.transasker.service.WordServiceTest.WordServiceImplTestContextConfiguration.*;
import static org.assertj.core.api.Assertions.assertThat;
@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
public class WordServiceTest {
    private final ModelMapper modelMapper = new ModelMapper();
    private final Type listType = new TypeToken<List<WordDto>>() {
    }.getType();

    @Autowired
    private WordService wordService;

    @Before
    public void setUp() throws Exception {
        User user = new User();
        user.setUsername("Green");
        Word word = Word.builder()
                .sourceWord("Hello")
                .targetWord("Привет")
                .fromLang(Languages.EN)
                .toLang(Languages.RU)
                .build();
        Word word1 = Word.builder()
                .sourceWord("Hi")
                .targetWord("Привет")
                .fromLang(Languages.EN)
                .toLang(Languages.RU)
                .build();
        Profile userProfile = Profile.builder().tgUsername("Admin").user(user).build();

        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        Mockito.when(wordRepository.getWordsBySourceWordIgnoreCase("Hello"))
                .thenReturn(Collections.singletonList(word));
        Mockito.when(profileService.getProfileByUser(user)).
                thenReturn(userProfile);
        Mockito.when(profileService.addWordsToProfileAndUpdateProfile(userProfile, Sets.newHashSet(word))).
                thenReturn(userProfile);
        Mockito.when(translatorService.callUrlAndParseResult(Languages.EN, Languages.RU, "hi")).
                thenReturn(Collections.singletonList(word1));

    }

    @Test
    public void getWordTranslationSuccFromDB() throws Exception {
        Word word = Word.builder()
                .sourceWord("Hello")
                .targetWord("Привет")
                .fromLang(Languages.EN)
                .toLang(Languages.RU)
                .build();
        List<Word> words = Collections.singletonList(word);
        List<WordDto> resultList = modelMapper.map(words, listType);
        List<WordDto> wordTranslation = wordService.getWordTranslation("Hello", Languages.EN, Languages.RU);
        assertThat(wordTranslation).containsAll(resultList);
    }

    @Test
    public void getWordTranslationSuccFromTranslator() throws Exception {
        Word word = Word.builder()
                .sourceWord("Hi")
                .targetWord("Привет")
                .fromLang(Languages.EN)
                .toLang(Languages.RU)
                .build();
        List<Word> words = Collections.singletonList(word);
        List<WordDto> resultList = modelMapper.map(words, listType);
        List<WordDto> wordTranslation = wordService.getWordTranslation("Hi", Languages.EN, Languages.RU);
        assertThat(wordTranslation).containsAll(resultList);
    }

    @TestConfiguration
    static class WordServiceImplTestContextConfiguration {
        @MockBean
        static WordRepository wordRepository;
        @MockBean
        static TranslatorService translatorService;
        @MockBean
        static ProfileService profileService;
        @MockBean
        static UserService userService;

        @Bean
        public WordService wordService() {
            return new WordServiceImpl(wordRepository, translatorService, profileService, userService);
        }
    }
}

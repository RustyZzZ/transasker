package com.transquiz.transasker.service;

import com.google.common.collect.Sets;
import com.transquiz.transasker.model.Languages;
import com.transquiz.transasker.model.Profile;
import com.transquiz.transasker.model.Word;
import com.transquiz.transasker.model.security.User;
import com.transquiz.transasker.repository.ProfileRepository;
import com.transquiz.transasker.service.impls.ProfileServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ProfileServiceTest {
    @Autowired
    private ProfileService profileService;

    @Before
    public void setUp() {
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
        Profile userProfile = Profile.builder().tgUsername("Admin").user(user).words(new HashSet<>()).build();
        Profile updatedUserProfile = Profile.builder()
                .tgUsername("Admin")
                .user(user)
                .words(Sets.newHashSet(word))
                .build();
        Profile doubleWordUserProfile = Profile.builder()
                .tgUsername("Admin")
                .user(user)
                .words(Sets.newHashSet(word, word1))
                .build();


        Mockito.when(ProfileServiceImplTestContextConfiguration.profileRepository.getProfileByUser(userProfile.getUser()))
                .thenReturn(userProfile);
        Mockito.when(ProfileServiceImplTestContextConfiguration.profileRepository.save(updatedUserProfile))
                .thenReturn(updatedUserProfile);
        Mockito.when(ProfileServiceImplTestContextConfiguration.profileRepository.save(doubleWordUserProfile))
                .thenReturn(doubleWordUserProfile);
    }

    @Test
    public void getProfileByUserWhenUserHasProfile() {
        User user = new User();
        user.setUsername("Green");
        Profile successProfile = profileService.getProfileByUser(user);
        assertThat(successProfile.getUser()).isEqualTo(user);
        assertThat(successProfile.getTgUsername()).isEqualTo("Admin");
    }

    @Test
    public void getProfileByUserWhenUserHasNotProfile() {
        User user = new User();
        user.setUsername("Green1");
        Profile profile = profileService.getProfileByUser(user);
        assertThat(profile.getUser()).isEqualTo(user);
        assertThat(profile.getTgUsername()).isNullOrEmpty();
    }

    @Test
    public void addWordsToProfileAndUpdateProfileWithoutWords() {
        User user = new User();
        user.setUsername("Green");
        Word word = Word.builder()
                .sourceWord("Hello")
                .targetWord("Привет")
                .fromLang(Languages.EN)
                .toLang(Languages.RU)
                .build();
        Profile userProfile = Profile.builder().tgUsername("Admin").user(user).build();
        Profile returnedProfile = profileService.addWordsToProfileAndUpdateProfile(userProfile, Sets.newHashSet(word));
        assertThat(returnedProfile.getUser().getUsername()).isEqualTo(user.getUsername());
        assertThat(returnedProfile.getWords()).contains(word);
    }

    @Test
    public void addWordsToProfileAndUpdateProfileWithSameWord() {
        User user = new User();
        user.setUsername("Green");
        Word word = Word.builder()
                .sourceWord("Hello")
                .targetWord("Привет")
                .fromLang(Languages.EN)
                .toLang(Languages.RU)
                .build();
        Profile userProfile = Profile.builder().tgUsername("Admin").user(user).words(Sets.newHashSet(word)).build();
        Profile returnedProfile = profileService.addWordsToProfileAndUpdateProfile(userProfile, Sets.newHashSet(word));
        assertThat(returnedProfile.getUser().getUsername()).isEqualTo(user.getUsername());
        assertThat(returnedProfile.getWords()).containsOnly(word);
    }

    @Test
    public void addWordsToProfileAndUpdateProfileWithOneExistingOneNot() {
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
        Profile userProfile = Profile.builder().tgUsername("Admin").user(user).words(Sets.newHashSet(word)).build();
        Profile returnedProfile = profileService.addWordsToProfileAndUpdateProfile(userProfile, Sets.newHashSet(word, word1));
        assertThat(returnedProfile.getUser().getUsername()).isEqualTo(user.getUsername());
        assertThat(returnedProfile.getWords()).contains(word, word1);
    }

    @TestConfiguration
    static class ProfileServiceImplTestContextConfiguration {
        @MockBean
        private static ProfileRepository profileRepository;

        @Bean
        public ProfileService profileService() {
            return new ProfileServiceImpl(profileRepository);
        }
    }

}

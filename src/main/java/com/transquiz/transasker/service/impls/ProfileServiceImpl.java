package com.transquiz.transasker.service.impls;

import com.transquiz.transasker.model.Profile;
import com.transquiz.transasker.model.Word;
import com.transquiz.transasker.model.security.User;
import com.transquiz.transasker.repository.ProfileRepository;
import com.transquiz.transasker.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Profile getProfileByUser(User user) {
        Profile profileByUser = profileRepository.getProfileByUser(user);
        return !Objects.isNull(profileByUser) ? profileByUser : getNewProfile(user);
    }

    private Profile getNewProfile(User user) {
        return Profile.builder().user(user).words(new HashSet<>()).build();
    }

    @Override
    public Profile addWordsToProfileAndUpdateProfile(Profile profile, Set<Word> words) {
        if (!CollectionUtils.isEmpty(profile.getWords())) {
            profile.getWords().addAll(words);
        } else {
            profile.setWords(words);
        }
        return profileRepository.save(profile);
    }

}

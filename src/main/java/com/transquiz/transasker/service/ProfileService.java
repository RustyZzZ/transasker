package com.transquiz.transasker.service;

import com.transquiz.transasker.model.Profile;
import com.transquiz.transasker.model.Word;
import com.transquiz.transasker.model.security.User;

import java.util.Set;

public interface ProfileService {
    Profile getProfileByUser(User user);

    Profile getProfileByTelegramUsername(String username, int chatId);

    Profile addWordsToProfileAndUpdateProfile(Profile profile, Set<Word> words);

    Profile getProfileByTelegramUsernameWithoutChatId(String telegramName);
}

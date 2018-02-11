package com.transquiz.telegramBot.service;

import com.transquiz.telegramBot.model.BotUser;
import com.transquiz.telegramBot.model.Chat;

public interface CommandService {


    void quiz(Chat chat, BotUser fromUser);

    void quit(Chat chat, BotUser fromUser);

    void next(Chat chat, BotUser fromUser);

    void answer(Chat chat, BotUser fromUser);

    void unknown(Chat chat, BotUser fromUser);

    void help(Chat chat, BotUser fromUser);
}

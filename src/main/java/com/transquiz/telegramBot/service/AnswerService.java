package com.transquiz.telegramBot.service;

import com.transquiz.telegramBot.model.Message;

public interface AnswerService {
    Message translateWordForPrivateTgUser(int chatId, String text, String tgUsername);

    Message translateWordForPublic(int chatId, String text);
}

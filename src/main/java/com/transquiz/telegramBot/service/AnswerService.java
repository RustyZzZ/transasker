package com.transquiz.telegramBot.service;

import com.transquiz.telegramBot.model.Chat;
import com.transquiz.telegramBot.model.Message;
import com.transquiz.transasker.model.Word;

public interface AnswerService {
    Message translateWordForPrivateTgUser(int chatId, String text, String tgUsername);

    Message translateWordForPublic(int chatId, String text);

    Message sendMessage(int chat_id, String text);

    void askWord(int id, Word wordToAsk, boolean wasAnswerCorrect, boolean isFirst);

    void tellAboutHelp(Chat chat);
}

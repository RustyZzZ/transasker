package com.transquiz.telegramBot.service.impls;

import com.transquiz.telegramBot.model.BotUser;
import com.transquiz.telegramBot.model.Chat;
import com.transquiz.telegramBot.service.AnswerService;
import com.transquiz.telegramBot.service.CommandService;
import com.transquiz.transasker.model.Word;
import com.transquiz.transasker.service.ProfileService;
import com.transquiz.transasker.service.QuizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.transquiz.transasker.util.Constants.Modes;

@Service
public class CommandServiceImpl implements CommandService {
    private final AnswerService answerService;
    private final QuizerService quizerService;
    private final ProfileService profileService;

    @Autowired
    public CommandServiceImpl(AnswerService answerService, QuizerService quizerService, ProfileService profileService) {
        this.answerService = answerService;
        this.quizerService = quizerService;
        this.profileService = profileService;
    }

    @Override
    public void quiz(Chat chat, BotUser fromUser) {
        profileService.setModeOfProfile(fromUser.getUsername(), Modes.QUIZ);
        Word wordToAsk = quizerService.getRandomQuizWordForTelegramName(fromUser.getUsername());
        answerService.askWord(chat.getId(), wordToAsk, false, false);
    }

    @Override
    public void quit(Chat chat, BotUser fromUser) {
        profileService.setModeOfProfile(fromUser.getUsername(), Modes.TRANSLATE);
    }

    @Override
    public void next(Chat chat, BotUser fromUser) {
        //TBD
    }

    @Override
    public void answer(Chat chat, BotUser fromUser) {
        //TBD
    }

    @Override
    public void unknown(Chat chat, BotUser fromUser) {
        answerService.sendMessage(chat.getId(), "sry bro((((");
    }

    @Override
    public void help(Chat chat, BotUser fromUser) {
        answerService.tellAboutHelp(chat);

    }
}

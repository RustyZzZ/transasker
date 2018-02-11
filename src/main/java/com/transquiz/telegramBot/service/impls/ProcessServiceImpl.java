package com.transquiz.telegramBot.service.impls;

import com.transquiz.telegramBot.model.*;
import com.transquiz.telegramBot.service.AnswerService;
import com.transquiz.telegramBot.service.CommandService;
import com.transquiz.telegramBot.service.ProcessService;
import com.transquiz.transasker.model.Word;
import com.transquiz.transasker.service.ProfileService;
import com.transquiz.transasker.service.QuizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

import static com.transquiz.transasker.util.Constants.Commands;
import static com.transquiz.transasker.util.Constants.Modes;

@Service
public class ProcessServiceImpl implements ProcessService {
    private final AnswerService answerService;
    private final ProfileService profileService;
    private final CommandService commandService;
    private final QuizerService quizerService;

    @Autowired
    public ProcessServiceImpl(AnswerService answerService, ProfileService profileService, CommandService commandService, QuizerService quizerService) {
        this.answerService = answerService;
        this.profileService = profileService;
        this.commandService = commandService;
        this.quizerService = quizerService;
    }

    @Override
    public void processUpdate(Update update) {
        Message message = update.getMessage();
        List<MessageEntity> entities = message.getEntities();
        if (CollectionUtils.isEmpty(entities)) {
            processByMode(message);
        } else {
            MessageEntity command = entities.stream()
                    .filter(s -> s.getType().equals("bot_command"))
                    .findAny()
                    .orElse(null);
            if (command != null) {
                processCommand(command, message);
            } else {
                //TBD entity, but not command
            }
        }

    }

    private void processByMode(Message message) {
        String tgUsername = message.getFrom().getUsername();
        switch (profileService.getModeByProfile(tgUsername)) {
            case Modes.TRANSLATE: {
                translateWordFromMessageForUser(message);
                break;
            }
            case Modes.QUIZ: {
                processQuiz(message);
                break;
            }
            default: {
                //TBD Unknown mode
            }
        }
    }

    private void processQuiz(Message message) {
        String tgUsername = message.getFrom().getUsername();
        String previousAnswer = message.getText();
        boolean wasAnswerCorrect = quizerService.processAnswer(tgUsername, previousAnswer);
        Word wordToAsk;
        if (!wasAnswerCorrect) {
            wordToAsk = quizerService.getCurrentWord(tgUsername);
            if (wordToAsk == null) {
                forgotWord(message);
                return;
            }
        } else {
            wordToAsk = quizerService.getRandomQuizWordForTelegramName(tgUsername);
        }
        answerService.askWord(message.getChat().getId(), wordToAsk, wasAnswerCorrect, false);
    }

    private void forgotWord(Message message) {
        String tgUsername = message.getFrom().getUsername();
        profileService.setModeOfProfile(tgUsername, Modes.TRANSLATE);
        answerService.sendMessage(message.getChat()
                .getId(), "Извини, я по каким-то причинам забыл что тебя спросил. " +
                "По этому я вернулся в режим переводчика." +
                " Если хочешь может опять начать просто написав /quiz");
    }

    private void processCommand(MessageEntity command, Message message) {
        String messageText = message.getText();
        String commandText = messageText.substring(command.getOffseit(), command.getLength());
        BotUser fromUser = message.getFrom();
        Chat chat = message.getChat();
        switch (commandText) {
            case Commands.QUIZ: {
                commandService.quiz(chat, fromUser);
                break;
            }
            case Commands.QUIT: {
                commandService.quit(chat, fromUser);
                break;
            }
            case Commands.NEXT: {
                commandService.next(chat, fromUser);
                break;
            }
            case Commands.ANSWER: {
                commandService.answer(chat, fromUser);
                break;
            }
            case Commands.HELP: {
                commandService.help(chat, fromUser);
                break;
            }
            default: {
                commandService.unknown(chat, fromUser);
                break;
            }
        }
    }

    private void translateWordFromMessageForUser(Message message) {
        String text = message.getText();
        Chat chat = message.getChat();
        int chatId = chat.getId();
        String chatType = chat.getType();
        String username = message.getFrom().getUsername();
        if (Objects.equals(chatType, "private")) {
            answerService.translateWordForPrivateTgUser(chatId, text, username);
        } else {
            answerService.translateWordForPublic(chatId, text);
        }
    }
}

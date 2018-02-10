package com.transquiz.telegramBot.service.impls;

import com.transquiz.telegramBot.model.*;
import com.transquiz.telegramBot.service.AnswerService;
import com.transquiz.telegramBot.service.ProcessService;
import com.transquiz.transasker.model.Profile;
import com.transquiz.transasker.service.ProfileService;
import com.transquiz.transasker.service.QuizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Service
public class ProcessServiceImpl implements ProcessService {
    private final AnswerService answerService;

    @Autowired
    public ProcessServiceImpl(AnswerService answerService) {
        this.answerService = answerService;
    }

    @Autowired
    private ProfileService profileService;

    @Autowired
    QuizerService quizerService;

    @Override
    public ApiResult<Message> processUpdate(Update update) {
        Message message = update.getMessage();
        List<MessageEntity> entities = message.getEntities();
        ApiResult<Message> messageApiResult;
        BotUser fromUser = message.getFrom();
        Chat chat = message.getChat();
        Profile profile = profileService.getProfileByTelegramUsername(fromUser.getUsername(), chat.getId());
        if (CollectionUtils.isEmpty(entities)) {
            messageApiResult = translateWordFromMessageForUser(message);
        } else {
            MessageEntity command = entities.stream()
                    .filter(s -> s.getType().equals("bot_command"))
                    .findAny()
                    .orElse(null);
            if (command != null) {
                String messageText = message.getText();
                String commandText = messageText.substring(command.getOffseit(), command.getLength());
                switch (commandText) {
                    case "/quiz": {
                        profile.setMode("quiz");
                        answerService.sendMessage(chat.getId(), "Переведи мне это слово" + quizerService
                                .getRandomQuizWordForTelegramName(fromUser.getUsername()));
                        break;
                    }
                    case "/quit": {
                        profile.setMode("translate");
                        break;
                    }
                    case "/next": {
                        //TBD
                        break;
                    }
                    case "/answer": {
                        //TBD
                        break;
                    }
                    default: {
                        answerService.sendMessage(chat.getId(), "sry bro((((");
                    }
                }
                messageApiResult = new ApiResult<>();//TBD
            } else {
                messageApiResult = new ApiResult<>();
            }
        }
        return messageApiResult;
    }

    private ApiResult<Message> translateWordFromMessageForUser(Message message) {
        ApiResult<Message> messageApiResult = new ApiResult<>();
        String text = message.getText();
        Chat chat = message.getChat();
        int chatId = chat.getId();
        String chatType = chat.getType();
        String username = message.getFrom().getUsername();
        if (Objects.equals(chatType, "private")) {
            messageApiResult.setResult(answerService.translateWordForPrivateTgUser(chatId, text, username));
        } else {
            messageApiResult.setResult(answerService.translateWordForPublic(chatId, text));
        }
        messageApiResult.setOk(messageApiResult.getResult() != null);
        return messageApiResult;
    }
}

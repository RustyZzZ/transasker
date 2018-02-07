package com.transquiz.telegramBot.service.impls;

import com.transquiz.telegramBot.model.*;
import com.transquiz.telegramBot.service.AnswerService;
import com.transquiz.telegramBot.service.ProcessService;
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

    @Override
    public ApiResult<Message> processUpdate(Update update) {
        Message message = update.getMessage();
        List<MessageEntity> entities = message.getEntities();
        ApiResult<Message> messageApiResult;
        if (CollectionUtils.isEmpty(entities)) {
            messageApiResult = translateWordFromMessageForUser(message);
        } else {
            messageApiResult = new ApiResult<>();
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

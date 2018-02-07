package com.transquiz.telegramBot.service;

import com.transquiz.telegramBot.model.ApiResult;
import com.transquiz.telegramBot.model.Message;
import com.transquiz.telegramBot.model.Update;

public interface ProcessService {
    ApiResult<Message> processUpdate(Update update);
}

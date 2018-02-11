package com.transquiz.telegramBot.service;

import com.transquiz.telegramBot.model.Update;

public interface ProcessService {
    void processUpdate(Update update);
}

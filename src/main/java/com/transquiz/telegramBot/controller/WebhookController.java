package com.transquiz.telegramBot.controller;

import com.transquiz.telegramBot.model.ApiResult;
import com.transquiz.telegramBot.model.Message;
import com.transquiz.telegramBot.model.Update;
import com.transquiz.telegramBot.service.ProcessService;
import com.transquiz.telegramBot.service.impls.AnswerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebhookController {
    private final AnswerServiceImpl responseServiceImpl;
    private final ProcessService processService;

    @Autowired
    public WebhookController(AnswerServiceImpl responseServiceImpl, ProcessService processService) {
        this.responseServiceImpl = responseServiceImpl;
        this.processService = processService;
    }

    @PostMapping("/botHook")
    public ApiResult<Message> testHook(@RequestBody Update update) throws Exception {
        System.out.println(update.toString());
        return processService.processUpdate(update);
    }

}

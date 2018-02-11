package com.transquiz.telegramBot.controller;

import com.transquiz.telegramBot.model.Update;
import com.transquiz.telegramBot.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebhookController {
    private final ProcessService processService;

    @Autowired
    public WebhookController(ProcessService processService) {
        this.processService = processService;
    }

    @PostMapping("/botHook")
    public void testHook(@RequestBody Update update) throws Exception {
        System.out.println(update.toString());
        processService.processUpdate(update);
    }

}

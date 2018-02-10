package com.transquiz.telegramBot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MessageEntity {
    @JsonProperty("type")
    private String type;
    @JsonProperty("offset")
    private int offseit;
    @JsonProperty("length")
    private int length;
    @JsonProperty("url")
    private String url;
    @JsonProperty("user")
    private BotUser user;
}

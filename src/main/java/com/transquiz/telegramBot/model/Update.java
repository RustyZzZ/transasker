package com.transquiz.telegramBot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Update {
    @JsonProperty("update_id")
    private int updateId;

    @JsonProperty("message")
    private Message message;
}

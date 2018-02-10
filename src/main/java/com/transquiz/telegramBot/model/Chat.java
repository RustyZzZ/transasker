package com.transquiz.telegramBot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Chat {
    @JsonProperty("id")
    private int id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("username")
    private String username;

    @JsonProperty("title")
    private String title;

}

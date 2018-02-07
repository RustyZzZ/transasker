package com.transquiz.telegramBot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ApiResult<T> {

    @JsonProperty("ok")
    private boolean ok;

    @JsonProperty("description")
    private String description;

    @JsonProperty("error_code")
    private int errorCode;

    @JsonProperty("result")
    private T result;
}

package com.transquiz.telegramBot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Message {
    @JsonProperty("message_id")
    private int messageId;

    @JsonProperty("from")
    private BotUser from;

    @JsonProperty("date")
    private int date;

    @JsonProperty("chat")
    private Chat chat;

    @JsonProperty("forward_from")
    private BotUser forwardFrom;

    @JsonProperty("forward_date")
    private int forwardDate;

    @JsonProperty("reply_to_message")
    private Message replyToMessage;

    @JsonProperty("text")
    private String text;

    @JsonProperty("entities")
    private List<MessageEntity> entities;

    private transient MessageType type;

    public MessageType getType() {
        if (type == null)
            determineType();
        return type;
    }

    private void determineType() {
        if (text != null) {
            type = MessageType.TEXT;
        } else {
            type = MessageType.UNKNOWN;
        }
    }

    public enum MessageType {
        TEXT, AUDIO, DOCUMENT, PHOTO, STICKER, VIDEO, VOICE, CONTACT, LOCATION, NEW_CHAT_PARTICIPANT, LEFT_CHAT_PARTICIPANT, NEW_CHAT_TITLE, NEW_CHAT_PHOTO, DELETE_CHAT_PHOTO, GROUP_CHAT_CREATED, UNKNOWN
    }
}

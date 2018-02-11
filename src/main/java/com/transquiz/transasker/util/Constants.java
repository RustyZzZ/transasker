package com.transquiz.transasker.util;

import com.google.common.collect.ImmutableMap;

public class Constants {
    public static class Commands {
        public static final String QUIZ = "/quiz";
        public static final String QUIT = "/quit";
        public static final String NEXT = "/next";
        public static final String ANSWER = "/answer";
        public static final String HELP = "/help";
        public static final ImmutableMap<String, String> commandDescriptionMap = ImmutableMap.<String, String>builder()
                .put(QUIZ, "Начинает опрос по тем словам, какие ты его спрашивал(ла)")
                .put(QUIT, "Заканчивает опрос и он переходит в режим переводчика")
                .put(NEXT, "Переходит к следующему слову не говоря правильного ответа(может еще раз спросить это слово)")
                .put(ANSWER, "Переходит к следующему слову, и говорит правильный ответ(больше не спрашивает это слово)")
                .put(HELP, "Выводит список команд")
                .build();
    }

    public static class Modes {
        public static final String QUIZ = "quiz_mode";
        public static final String TRANSLATE = "translate_mode";
    }
}

package com.transquiz.telegramBot.service.impls;

import com.transquiz.telegramBot.model.Chat;
import com.transquiz.telegramBot.model.Message;
import com.transquiz.telegramBot.service.AnswerService;
import com.transquiz.transasker.dto.WordDto;
import com.transquiz.transasker.model.Languages;
import com.transquiz.transasker.model.Word;
import com.transquiz.transasker.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.transquiz.transasker.util.Constants.Commands.commandDescriptionMap;

@Service
public class AnswerServiceImpl implements AnswerService {
    private final RestTemplate restTemplate;
    private final WordService wordServiceImpl;

    @Value("${telegram.url}")
    private String telegramUrl;

    @Autowired
    public AnswerServiceImpl(RestTemplate restTemplate, WordService wordServiceImpl) {
        this.restTemplate = restTemplate;
        this.wordServiceImpl = wordServiceImpl;
    }

    public Message translateWordForPrivateTgUser(int chatId, String text, String tgUsername) {
        List<WordDto> wordTranslation;
        try {
            wordTranslation = wordServiceImpl.getWordTranslation(text, Languages.EN, Languages.RU, tgUsername, chatId);
        } catch (Exception e) {
            e.printStackTrace();
            return new Message();
        }
        String textMessage = createTextMessageForWordTraslation(wordTranslation);
        return sendMessage(chatId, textMessage);
    }

    public Message translateWordForPublic(int chatId, String text) {
        List<WordDto> wordTranslation;
        try {
            wordTranslation = wordServiceImpl.getWordTranslationPublic(text, Languages.EN, Languages.RU);
        } catch (Exception e) {
            e.printStackTrace();
            return new Message();
        }

        String textMessage = createTextMessageForWordTraslation(wordTranslation);
        return sendMessage(chatId, textMessage);
    }

    private String createTextMessageForWordTraslation(List<WordDto> wordTranslation) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Хмммм, ты спросил (спросила) меня слово ")
                .append(makeBold("\"" + wordTranslation.get(0).getSourceWord() + "\""))
                .append("\n\n")
                .append("Мне известны такие переводы (перевод) этого слова:\n");
        for (WordDto wordDto : wordTranslation) {
            stringBuilder.append(makeItalic(wordDto.getTargetWord()))
                    .append("\n");
        }
        return stringBuilder.toString();
    }

    public Message sendMessage(int chat_id, String text) {
        String url = telegramUrl + buildMessage(chat_id, text);
        return restTemplate.getForObject(url, Message.class);

    }

    @Override
    public void askWord(int chatId, Word wordToAsk, boolean wasAnswerCorrect, boolean isFirst) {
        String sourceWord = wordToAsk.getSourceWord();
        if (!isFirst) {
            sendMessage(chatId, "Хммм щас посмотрю.");
            String message = wasAnswerCorrect
                    ? makeItalic("Да твой ответ был правильным.") + " А как переводится " + makeBold("\"" + sourceWord + "\"")
                    : makeItalic("Нет ты ошибся (ошиблась).") + " Подумай хорошо. Как переводится " + makeBold("\"" + sourceWord + "\"");
            sendMessage(chatId, message);
        } else {
            sendMessage(chatId, "Переведи мне слово " + makeBold("\"" + sourceWord + "\""));
        }
    }

    @Override
    public void tellAboutHelp(Chat chat) {
        StringBuilder stringBuilder = new StringBuilder().append("Смотри что умею: \n");
        String commands = commandDescriptionMap.entrySet()
                .stream()
                .map((this::mapEntryToString))
                .collect(Collectors.joining("\n"));
        stringBuilder.append(commands);
        sendMessage(chat.getId(), stringBuilder.toString());
    }

    private String mapEntryToString(Map.Entry<String, String> entry) {
        return makeBold(entry.getKey()) + " - " + entry.getValue();
    }

    private String buildMessage(int chat_id, String message) {
        return "/sendMessage" +
                "?chat_id=" + chat_id +
                "&text=" + message +
                "&parse_mode=Markdown";
    }

    private String makeBold(String string) {
        return "*" + string + "*";
    }

    private String makeItalic(String string) {
        return "_" + string + "_";
    }

    private String makeMonospaced(String string) {
        return "```" + string + "```";
    }

}

package com.transquiz.transasker.service.impls;

import com.transquiz.transasker.model.Languages;
import com.transquiz.transasker.model.Word;
import com.transquiz.transasker.service.JsonWordParser;
import com.transquiz.transasker.service.TranslatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

@Service
public class TranslatorServiceImpl implements TranslatorService {
    private final JsonWordParser jsonWordParser;

    @Autowired
    public TranslatorServiceImpl(JsonWordParser jsonWordParser) {
        this.jsonWordParser = jsonWordParser;
    }

    public List<Word> callUrlAndParseResult(Languages langFrom, Languages langTo, String word) throws Exception {

        String url = getUrl(langFrom, langTo, word, true, true);
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        String toString = response.toString();
        return jsonWordParser.parseJsonWithWordsToListOfWords(toString);
    }

    private String getUrl(Languages langFrom, Languages langTo, String word, boolean trans, boolean alterTrans) throws UnsupportedEncodingException {
        return "https://translate.googleapis.com/translate_a/single?" +
                "client=gtx&" +
                "sl=" + langFrom.toString() +
                "&tl=" + langTo.toString() +
                (trans ? "&dt=t" : "") +
                (alterTrans ? "&dt=at" : "") +
                "&dj=1" +
                "&q=" + URLEncoder.encode(word, "UTF-8");
    }
}


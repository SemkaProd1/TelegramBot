package bot;


import com.sun.deploy.util.StringUtils;
import database.DatabaseConnector;
import database.Pinned;
import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.telegrambots.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegraph.api.methods.GetPageList;
import org.telegram.telegraph.api.objects.Page;
import org.telegram.telegraph.api.objects.PageList;
import org.telegram.telegraph.exceptions.TelegraphException;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {

    private DatabaseConnector databaseConnector = new DatabaseConnector();

    public String authorization(String token) {
        JSONObject json = null;
        try {
            json = readJsonFromUrl("https://api.telegra.ph/getAccountInfo?access_token=" + token + "&fields=[\"auth_url\"]");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert json != null;
        json = (JSONObject) json.opt("result");
        return (String) json.get("auth_url");
    }

    public String regex(String s) {
        String regex = "^[0-9]{9}$";
        Pattern pat = Pattern.compile(regex);
        if (s.length() == 16) {
            s = s.substring(s.length() - 9);

            Matcher matcher = pat.matcher(s);
            if (matcher.find()) {
                return s;
            }
        } else s = "/start";
        return s;
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        }
    }


    public AnswerInlineQuery converteResultsToResponse(InlineQuery inlineQuery, String fromUser, String pmht) {
        AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery();
        answerInlineQuery.setInlineQueryId(inlineQuery.getId());
        answerInlineQuery.setCacheTime(0);
        if (pmht.equals("pm")) {
            answerInlineQuery.setResults(convertResultsPM(fromUser));
        }
        if (pmht.equals("ht")) {
            answerInlineQuery.setResults(convertResultsHT(fromUser));
        }
        if (pmht.equals("rf"))
        {
            answerInlineQuery.setResults(convertResultsRF(Integer.parseInt(fromUser)));
        }
        return answerInlineQuery;
    }

    private List<InlineQueryResult> convertResultsRF(int id) {
        List<InlineQueryResult> results = new ArrayList<>();
        InputTextMessageContent messageContent = new InputTextMessageContent();
        messageContent.enableWebPagePreview();
        messageContent.setMessageText("Привет! Тут ты сможешь получать необходимую для обучения информацию: \n \n https://telegram.me/educationChat_bot?start="+id);
        InlineQueryResultArticle article = new InlineQueryResultArticle();

        article.setInputMessageContent(messageContent);
        article.setId(Integer.toString(1));
        article.setTitle("Отправить Реферальную Ссылку");
        article.setThumbUrl("https://cdn.pbrd.co/images/HnywTrH.jpg");//ht

        article.setDescription("Нажмите сюда, чтобы отправить вашу ссылку");
        results.add(article);
        return  results;
    }

    private List<InlineQueryResult> convertResultsHT(String fromUser) {
        List<InlineQueryResult> results = new ArrayList<>();
        PageList pageList = null;
        try {
            pageList = getPageList((String) databaseConnector.getParamByUserName(fromUser, "token"));
        } catch (TelegraphException e) {
            e.printStackTrace();
        }
        assert pageList != null;
        List<Page> pages = pageList.getPages();
        List<String> titles = new ArrayList<>();
        List<String> descriptions = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < pageList.getTotalCount(); i++) {
            titles.add(pages.get(i).getTitle());
            descriptions.add(pages.get(i).getDescription());
            urls.add(pages.get(i).getUrl());
            InputTextMessageContent messageContent = new InputTextMessageContent();
            messageContent.enableWebPagePreview();
            messageContent.enableHtml(true);
            messageContent.setMessageText(urls.get(i));
            InlineQueryResultArticle article = new InlineQueryResultArticle();

            article.setInputMessageContent(messageContent);
            article.setId(Integer.toString(i));
            article.setTitle(titles.get(i));
            article.setThumbUrl("https://cdn.pbrd.co/images/HnywTrH.jpg");//ht

            article.setDescription(descriptions.get(i));
            results.add(article);
        }
        return results;
    }

    private List<InlineQueryResult> convertResultsPM(String fromUser) {
        List<InlineQueryResult> results = new ArrayList<>();
        int totalCount = databaseConnector.getNumberOfPinns(fromUser);
        List<String> titles = new ArrayList<>();
        List<Pinned> pins = databaseConnector.getAllPinsByUserName(fromUser);
        List<String> descriptions = new ArrayList<>();
        List<String> urls = new ArrayList<>();

        for (int i = 0; i < totalCount; i++) {
            titles.add(pins.get(i).getForwardFrom() + " :");
            descriptions.add(pins.get(i).getTextMessage());
            urls.add(pins.get(i).getTextMessage());

        }
        for (int i = 0; i < totalCount; totalCount--) {
            InputTextMessageContent messageContent = new InputTextMessageContent();
            messageContent.enableWebPagePreview();
            messageContent.enableHtml(true);
            messageContent.setMessageText(urls.get(totalCount - 1));
            InlineQueryResultArticle article = new InlineQueryResultArticle();

            article.setInputMessageContent(messageContent);
            article.setId(Integer.toString(totalCount - 1));
            article.setTitle(titles.get(totalCount - 1));
            article.setThumbUrl("https://cdn.pbrd.co/images/Hnyy2bO.jpg");//pm
            article.setDescription(descriptions.get(totalCount - 1));
            results.add(article);
        }
        return results;
    }

    private PageList getPageList(String token) throws TelegraphException {
        return new GetPageList(token)
                .setLimit(50)
                .execute();
    }

    public String getInfo(String userName) {
        List sa = databaseConnector.exists(userName);
        List results = new LinkedList();
        for (int i = 0; i < sa.size(); i++){
            results.add("\uD83D\uDE0E <a href='tg://user?id=" + databaseConnector.getParamByUserName(String.valueOf(sa.get(i)),"userId") +"'>" + sa.get(i) + "</a> \n");
        }
        return StringUtils.join(results, "");
    }
}

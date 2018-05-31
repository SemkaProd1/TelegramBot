package bot;


import database.DatabaseConnector;
import database.Pinned;
import org.telegram.telegrambots.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegraph.api.methods.GetPageList;
import org.telegram.telegraph.api.objects.Page;
import org.telegram.telegraph.api.objects.PageList;
import org.telegram.telegraph.exceptions.TelegraphException;

import java.util.ArrayList;
import java.util.List;

public class InlineContainer  {
    private DatabaseConnector databaseConnector = new DatabaseConnector();


    public AnswerInlineQuery converteResultsToResponse(InlineQuery inlineQuery, String fromUser, String pmht) {
        AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery();
        answerInlineQuery.setInlineQueryId(inlineQuery.getId());
        answerInlineQuery.setCacheTime(0);
        if (pmht.equals("pm")) {
            answerInlineQuery.setResults(convertResultsPM(fromUser));
        }
        if (pmht.equals("ht")){
            answerInlineQuery.setResults(convertResultsHT(fromUser));
        }
        return answerInlineQuery;
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

        for (int i = 0; i <totalCount; i++){
            titles.add(pins.get(i).getForwardFrom()+ " :");
            descriptions.add(pins.get(i).getTextMessage());
            urls.add(pins.get(i).getTextMessage());

        }
        for (int i = 0; i < totalCount; totalCount--) {
            InputTextMessageContent messageContent = new InputTextMessageContent();
            messageContent.enableWebPagePreview();
            messageContent.enableHtml(true);
            messageContent.setMessageText(urls.get(totalCount-1));
            InlineQueryResultArticle article = new InlineQueryResultArticle();

            article.setInputMessageContent(messageContent);
            article.setId(Integer.toString(totalCount-1));
            article.setTitle(titles.get(totalCount-1));
            article.setThumbUrl("https://cdn.pbrd.co/images/Hnyy2bO.jpg");//pm
            article.setDescription(descriptions.get(totalCount-1));
            results.add(article);
        }
        return results;
    }

    private PageList getPageList(String token) throws TelegraphException {
        return new GetPageList(token)
                .setLimit(50)
                .execute();
    }
}
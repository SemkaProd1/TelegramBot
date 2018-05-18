
import database.DatabaseConnector;
import database.UserEntity;
import org.json.JSONObject;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;


import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Math.toIntExact;

public class TestClass extends TelegramLongPollingBot {
    private boolean home_tasting_processing;
    private final String token = "575468488:AAHvi9kUv_2SDiH4tEwpfQP23_P_OgHwKa0";
    private String url = "https://api.telegram.org/bot575468488:AAHvi9kUv_2SDiH4tEwpfQP23_P_OgHwKa0/getupdates";
    private UserEntity user = new UserEntity();
    private DatabaseConnector databaseConnector = new DatabaseConnector();


    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new TestClass());

            System.out.println("started.");

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "educationChat_bot";
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {

        Message message = update.getMessage();
        if (home_tasting_processing){
            if (user.getUserName() != null && user.getAuthorName() != null){
                account(update);
            }
            else if (user.getUserName() != null && user.getAuthorName() == null){
                user.setAuthorName(saveText(update, user.getUserName()));
                account(update);
            }
            else if (user.getUserName() == null) {
                user.setUserName(saveText(update, ""));
                account(update);
            }


        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            if (message.getText().equals("/help") || message.getText().equals("/help@" + getBotUsername())) {
                if (message.isSuperGroupMessage()) {


                    sendMsgToChat(update, "sad");

                    markup(update);
                } else {
                   sendMsgToPrivate(update, "sad");
                }
            }
            if (message.getText().equals("/start")) {
                markup(update);
            }
            if (message.getText().equals("ss")) {
                account(update);
            }

            if (update.hasMessage() && update.getMessage().hasText()) {
                long chat_id = update.getMessage().getChatId();
                if (message.getText().equals("Домашнее задание")) {
                    SendMessage sendMessage = new SendMessage().setChatId(chat_id).setText("вот, смотри, работает");
                    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                    List<InlineKeyboardButton> rowInline = new ArrayList<>();
                    rowInline.add(new InlineKeyboardButton().setText("Максим!").setCallbackData("JOIN_PREMIUM").setUrl(url));
                    rowInline.add(new InlineKeyboardButton().setText("Максим!").setCallbackData("JOIN_PREMIUM").setUrl(url));
                    rowInline.add(new InlineKeyboardButton().setText("Максим!").setCallbackData("JOIN_PREMIUM").setUrl(url));
                    rowInline.add(new InlineKeyboardButton().setText("Максим!").setCallbackData("JOIN_PREMIUM").setUrl(url));
                    List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
                    rowInline2.add(new InlineKeyboardButton().setText("Максим!").setCallbackData("JOIN_PREMIUM").setUrl(url));
                    rowInline2.add(new InlineKeyboardButton().setText("Максим!").setCallbackData("JOIN_PREMIUM").setUrl(url));
                    List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
                    rowInline3.add(new InlineKeyboardButton().setText("Максим!!!!!!!!!!!").setCallbackData("JOIN_PREMIUM").setUrl(url));
                    rowsInline.add(rowInline);
                    rowsInline.add(rowInline2);
                    rowsInline.add(rowInline3);
                    markupInline.setKeyboard(rowsInline);
                    sendMessage.setReplyMarkup(markupInline);
                    try {
                        execute(sendMessage); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }

            if (message.getText().equals("Домашнее задание")) {
                markupHT(update);


            }
            if (message.getText().equals("Назад")) {
                markup(update);
            }
        }
        } else if (update.hasCallbackQuery()) {
            // Set variables
            String call_data = update.getCallbackQuery().getData();

            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();

            if (call_data.equals("JOIN_PREMIUM")) {
                System.out.println("haha");
                sendMsgToPrivate(update, "haha");
            }
            if (call_data.equals("authorization")) {
                user.setUserName((String) databaseConnector.getStateByTelegramName("username", update.getCallbackQuery().getFrom().getUserName()));
                AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
                answerCallbackQuery.setUrl(authorization((String) databaseConnector.getStateByUserName(user.getUserName(), "token")));
                answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());
                answerCallbackQuery.setText("sa?");
                try {
                    execute(answerCallbackQuery);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }


//                ifNode contentNode= new NodeText("Спасибо ");
//                List<Node> content = new ArrayList<>();
//                content.add(contentNode);
//
//                Page page = new CreatePage(TLGRPH_TOKEN, "My title", content)
//                        .setAuthorName("Random author")
//                        .setReturnContent(true)
//                        .execute();

//        if (message.getText().equals("Добавить домашнее задание")){
//                homeTask(update);
//        }
    }


    private String saveText(Update update, String lastMsg) {
        String root_text = update.getMessage().getText();
        if (root_text.equals(lastMsg)){
            return null;
        }
        return root_text;
    }
    private void account(Update update) {
        home_tasting_processing = true;
        if (user.getUserName() == null) {
            sendMsgToPrivate(update, "Введите userName");

        }
        else if (user.getUserName() != null && user.getAuthorName() == null) {
            sendMsgToPrivate(update, "Введите autorName");
        }
        else if (user.getUserName() != null && user.getAuthorName() != null) {
            user.setTelegramName(update.getMessage().getFrom().getUserName());
            SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId()).setText("Отлично! Аккаунт создан. Вот ваша ссылка для авторизации этого аккаунта"); // Create a message object object
            JSONObject json = null;
            String url2 = "https://api.telegra.ph/createAccount?short_name=" + user.getUserName() + "&author_name=" + user.getAuthorName();
            System.out.println(url2);
            try {
                json = JsonReader.readJsonFromUrl(url2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert json != null;
            JSONObject result = (JSONObject) json.opt("result");
            user.setToken((String) result.get("access_token"));
            user.setisAdmin(true);
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            List<InlineKeyboardButton> rowline = new ArrayList<>();
            List<InlineKeyboardButton> rowline2 = new ArrayList<>();
            rowline.add(new InlineKeyboardButton().setText("Войти как " + user.getUserName()).setCallbackData("authorization"));
            rowline2.add(new InlineKeyboardButton().setText("создать страницу").setCallbackData("create_page"));
            databaseConnector.insertUser(user);
            rowsInline.add(rowline);
            rowsInline.add(rowline2);
            markupInline.setKeyboard(rowsInline);
            message.setReplyMarkup(markupInline);
            home_tasting_processing = false;
            user.reset();
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
        private void markup(Update update) {
        SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId()).setText("keyboard"); // Create a message object object

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Домашнее задание");
        row.add("Row 1 Button 3");
        keyboard.add(row);
        row = new KeyboardRow();
        row.add("Row 2 Button 1");
        row.add("Row 2 Button 2");
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }




    private void markupHT(Update update) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId()).setText("В этом меню вы можете управлять домшними заданиями");
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Добавить домашнее задание");
        keyboard.add(row);
        row = new KeyboardRow();
        row.add("Все домашние задания");
        keyboard.add(row);
        row = new KeyboardRow();
        row.add("Назад");
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }



    private void sendMsgToChat(Update update, String text) {

        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true)
                .setText(text)
                .setChatId(String.valueOf((message.getChatId())));
        if (message.isCommand()) {
            //deleteMsg(message.getChatId(), message.getMessageId());
        }
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    private void sendMsgToPrivate(Update update, String text) {
        int id = 0;
        SendMessage sendMessage = new SendMessage();
        if(update.hasCallbackQuery()){
            id = update.getCallbackQuery().getFrom().getId();
        }
        else if (update.getMessage().hasText()){
            id = update.getMessage().getFrom().getId();
        }
        sendMessage.enableMarkdown(true)
                    .setText(text)
                    .setChatId(String.valueOf((id)));
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String  authorization(String token){
        JSONObject json = null;
        try {
            json = JsonReader.readJsonFromUrl("https://api.telegra.ph/getAccountInfo?access_token=" + token +"&fields=[\"auth_url\"]");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert json != null;
        JSONObject result2 = (JSONObject) json.opt("result");
        return (String) result2.get("auth_url");
    }

    private void deleteMsg(long chat, long message){
        DeleteMessage new_message = new DeleteMessage()
                .setChatId(String.valueOf(chat))
                .setMessageId(toIntExact(message));
        try {
            execute(new_message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
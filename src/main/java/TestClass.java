
import org.json.JSONObject;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
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
import org.telegram.telegraph.api.methods.CreatePage;
import org.telegram.telegraph.api.objects.Node;
import org.telegram.telegraph.api.objects.NodeText;
import org.telegram.telegraph.api.objects.Page;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Math.toIntExact;

public class TestClass extends TelegramLongPollingBot {
    private boolean must_save_msg_inf;
    private boolean home_tasting_processing;
    private String token = "575468488:AAHvi9kUv_2SDiH4tEwpfQP23_P_OgHwKa0";
    int root_fwdMsg_ID =0;
    String userName = null;
    String autorName = null;
    String title = null;
    String TLGRPH_TOKEN = null;
    long root_CHAT_ID = 0;
    String url = "https://api.telegram.org/bot575468488:AAHvi9kUv_2SDiH4tEwpfQP23_P_OgHwKa0/getupdates";


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
            if (userName != null && autorName != null){
                authorization(update);
            }
            if (userName != null && autorName == null){
                autorName = saveText(update, userName);
                authorization(update);
            }
            if (userName == null) {
                userName = saveText(update, "");
                authorization(update);
            }


        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            if (message.getText().equals("/help") || message.getText().equals("/help@" + getBotUsername())) {
                if (message.isSuperGroupMessage()) {
                    sendMsgToChat(update, "Привет, я робот");
                    markup(update);
                } else {
                    sendMsgToPrivate(update, "Привет, я робот");
                }
            }
            if (message.getText().equals("/start")) {
                markup(update);
            }
            if (message.getText().equals("ss")) {
                authorization(update);
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

            if (message.getText().equals("/saveMsg") || message.getText().equals("/saveMsg@" + getBotUsername())) {
                must_save_msg_inf = true;
                sendMsgToPrivate(update, "Перешлите мне сообщение, которое нужно сохранить");
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
            if (call_data.equals("create_page")){

//                ifNode contentNode= new NodeText("Спасибо ");
//                List<Node> content = new ArrayList<>();
//                content.add(contentNode);
//
//                Page page = new CreatePage(TLGRPH_TOKEN, "My title", content)
//                        .setAuthorName("Random author")
//                        .setReturnContent(true)
//                        .execute();
            }
        }
//        if (message.getText().equals("Добавить домашнее задание")){
//                homeTask(update);
//        }

        if (must_save_msg_inf) {
            if (!message.isCommand()) {
                saveMsg(update);
            }
        }
    }


    private String saveText(Update update, String lastMsg) {
        String root_text = update.getMessage().getText();
        if (root_text.equals(lastMsg)){
            return null;
        }
        return root_text;
    }
    private void authorization(Update update) {
        home_tasting_processing = true;
        if (userName == null) {
            sendMsgToPrivate(update, "Введите userName");
        }
        if (userName != null && autorName == null) {
            sendMsgToPrivate(update, "Введите autorName");
        }
        if (userName != null && autorName != null) {
            SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId()).setText("Отлично! Аккаунт создан. Вот ваша ссылка для авторизации этого аккаунта"); // Create a message object object
            JSONObject json = null;
            String url2 = "https://api.telegra.ph/createAccount?short_name=" + userName + "&author_name=" + autorName;
            System.out.println(url2);
            try {
                json = JsonReader.readJsonFromUrl(url2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject result = (JSONObject) json.opt("result");
            TLGRPH_TOKEN = (String) result.get("access_token");
            System.out.println(TLGRPH_TOKEN);
            String TLGRPH_CONFIM_URL = (String) result.get("auth_url");
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            List<InlineKeyboardButton> rowline = new ArrayList<>();
            List<InlineKeyboardButton> rowline2 = new ArrayList<>();
            rowline.add(new InlineKeyboardButton().setText("Войти как " + userName).setUrl(TLGRPH_CONFIM_URL).setCallbackData("mm"));
            rowline2.add(new InlineKeyboardButton().setText("создать страницу").setCallbackData("create_page"));
            String url3 = "https://api.telegra.ph/getAccountInfo?access_token=" + TLGRPH_TOKEN +"&fields=[\"auth_url\"]";
            System.out.println(url3);
            try {
                json = JsonReader.readJsonFromUrl(url3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject result2 = (JSONObject) json.opt("result");
            String NEW_URL = (String) result2.get("auth_url");
            rowline2.add(new InlineKeyboardButton().setText("Войти с другого устройства").setUrl(NEW_URL).setCallbackData("all_pages"));
            rowsInline.add(rowline);
            rowsInline.add(rowline2);
            markupInline.setKeyboard(rowsInline);
            message.setReplyMarkup(markupInline);
            home_tasting_processing = false;
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

    private void saveMsg(Update update ) {
        Message command_test = update.getMessage();
        if (!command_test.isCommand() && must_save_msg_inf) {
            command_test = update.getMessage();
            root_fwdMsg_ID = command_test.getMessageId();
            root_CHAT_ID = command_test.getChatId();
            must_save_msg_inf = false;
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
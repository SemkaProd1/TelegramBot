
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


import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.incrementExact;
import static java.lang.Math.toIntExact;

public class TestClass extends TelegramLongPollingBot {
    private boolean must_save_msg_inf;
    private boolean must_save_msg_text;
    String root_text = null;
    private boolean home_tasting_processing;
    private String token = "575468488:AAHvi9kUv_2SDiH4tEwpfQP23_P_OgHwKa0";
    int root_fwdMsg_ID =0;
    String userName = null;
    String autorName = null;
    String title = null;
    long root_CHAT_ID = 0;
    String url = "https://api.telegram.org/bot575468488:AAHvi9kUv_2SDiH4tEwpfQP23_P_OgHwKa0/";


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
            if (userName != null && autorName != null && title == null){
                title = saveText(update, autorName);
                    homeTask(update);
            }

        }
        if (message.getText().equals("/help")|| message.getText().equals("/help@" + getBotUsername())){
            if(message.isSuperGroupMessage()) {
                sendMsgToChat(update, "Привет, я робот");
                markup(update);
            }else{
                sendMsgToPrivate(update, "Привет, я робот");
            }
        }
        if (message.getText().equals("/start")){
            markup(update);
        }
        if (message.getText().equals("/condb")) {

            String user_first_name = update.getMessage().getChat().getFirstName();
            String user_last_name = update.getMessage().getChat().getLastName();
            String user_username = update.getMessage().getChat().getUserName();
            long user_id = update.getMessage().getChat().getId();

            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            SendMessage message1 = new SendMessage().setChatId(chat_id).setText(message_text);
            try {
                sendMessage(message1);
                Browser.check(user_first_name, user_last_name, toIntExact(user_id), user_username);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        if (message.getText().equals("/saveMsg") || message.getText().equals("/saveMsg@" + getBotUsername())){
            must_save_msg_inf = true;
            sendMsgToPrivate(update, "Перешлите мне сообщение, которое нужно сохранить");
        }

        if (message.getText().equals("Домашнее задание")){
            markupHT(update);
        }
        if (message.getText().equals("Назад")){
            markup(update);
        }
        if (message.getText().equals("Добавить домашнее задание")){
                homeTask(update);
        }

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
        System.out.println(root_text);
        return root_text;
    }
    private void homeTask(Update update) {
        home_tasting_processing = true;
        if (userName == null) {
            sendMsgToPrivate(update, "Введите userName");
        }

        if (userName != null && autorName != null && title !=null) {
            sendMsgToPrivate(update, "Отлично!");



            SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId()).setText(""); // Create a message object object

            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add(new InlineKeyboardButton().setText(title).setCallbackData("chto eto"));
            rowsInline.add(rowInline);

            // Add it to the message
            markupInline.setKeyboard(rowsInline);
            message.setReplyMarkup(markupInline);
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


    private void hometask (Update update) {
        home_tasting_processing = true;
        if (userName == null) {
            sendMsgToPrivate(update, "Введите userName");
        }
        SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId()).setText("Вы можете создать новое домашнее задание или управлять старыми"); // Create a message object object

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Создать новое домашнее задание").setCallbackData("chto eto"));
        rowInline.add(new InlineKeyboardButton().setText("Все домашние задания").setUrl("http://telegra.ph/").setCallbackData("ctho eto"));
        rowsInline.add(rowInline);

        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
// Document doc = Jsoup.connect("http://telegra.ph/test-story-04-14").get();
//        String title = doc.select(".tl_article_header h1").text();

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

        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true)
                    .setText(text)
                    .setChatId(String.valueOf((message.getFrom().getId())));
        if (message.isCommand()){
            sendMessage.setReplyToMessageId(message.getMessageId());
        }
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
    private void homeTask2(Update update) {
        SendMessage message1 = new SendMessage().setChatId(update.getMessage().getChatId()).setText("Создайте аккаунт! можно будет иметь несколько.");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        String userName = update.getMessage().getFrom().getUserName();
        rowInline.add(new InlineKeyboardButton().setText("Создать аккаунт").setUrl("https://api.telegra.ph/createAccount?short_name=" +userName + "&author_name=" + userName).setCallbackData("chto eto"));

        rowsInline.add(rowInline);


        markupInline.setKeyboard(rowsInline);
        message1.setReplyMarkup(markupInline);
        try {
            execute(message1);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
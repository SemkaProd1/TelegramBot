package bot;

import database.DatabaseConnector;
import database.Pinned;
import database.User;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import org.telegram.telegraph.api.methods.CreateAccount;
import org.telegram.telegraph.api.methods.GetPageList;
import org.telegram.telegraph.api.objects.Account;
import org.telegram.telegraph.api.objects.PageList;
import org.telegram.telegraph.exceptions.TelegraphException;

import java.util.ArrayList;
import java.util.List;


public class TelegramBot extends TelegramLongPollingBot {


    private boolean pinned_message_processing;
    private boolean create_account_processing;
    private User user = new User();
    private Utilities utilities = new Utilities();
    private Pinned pinned = new Pinned();
    private DatabaseConnector databaseConnector = new DatabaseConnector();


    @Override
    public String getBotUsername() {
        return "educator_bot";
    }

    @Override
    public String getBotToken() {
        return "614209030:AAHaFWGE96VepGqVVvDXpyYkrzwqxLAqRzo";

    }

    @Override
    public void onUpdateReceived(Update update) {

        Message message = update.getMessage();

        if (create_account_processing) {
            if (user.getAuthorName() != null) {
                createAccount(update);
            } else if (user.getAuthorName() == null) {
                user.setUserName(update.getMessage().getFrom().getUserName());
                user.setAuthorName(saveText(update, user.getUserName()));
                createAccount(update);
            }
        }
        if (pinned_message_processing) {
            if (pinned.getMessageId() == 0) {
                savePin(update);
            }

        }
        if (update.hasInlineQuery()) {
            handleIncomingInlineQuery(update.getInlineQuery());
        }
        if (update.hasMessage() && update.getMessage().hasText()) {
            if (message.getText().equals("/help") || message.getText().equals("/help@" + getBotUsername())) {
                if (message.isSuperGroupMessage()) {

                    markup(update, "Это меню помощи");
                } else {
                    sendMsgToPrivate(update, "sad");

                }
            }
            if ((message.getText()).equals("/start " + utilities.regex(message.getText()))) {

                markup(update, "Поздравляю! Вы подключены к пользователю " + databaseConnector.getParamByUserId(Integer.parseInt(utilities.
                        regex(message.getText())), "userName").toString() +"\n\n" + "K сожалению, наш бот может работать только если у " +
                        "вас заполнено поле username в Telegram. Сейчас ваш юзернейм "+ update.getMessage().getFrom().getUserName()+
                        "'\n\nЕсли он 'null', пожалуйста, задайте его в настройках прямо сейчас!");
                sub_markup(update);
                subber(update, Integer.parseInt(utilities.regex(message.getText())));
            }
            if (message.getText().equals("/start")) {
                markup(update, "Привет! Если вы не преподаватель, попросите его о уникальной ссылке. \n" +
                        "Для того, чтобы начать пользоваться ботом, пожалуйста, настройте максимально свой аккаунт!"+"\n\n" + "K сожалению, наш бот " +
                        "может работать только если у вас заполнено поле username в Telegram. Сейчас ваш юзернейм = '"+ update.getMessage()
                        .getFrom().getUserName()+ "'\n\nЕсли он 'null', пожалуйста, задайте его в настройках прямо сейчас!");
            }
            if (message.getText().equals("Сохраненные сообщения")) {

                pinned(update);
            }


            if (message.getText().equals("Домашние задания")) {
                homeTask(update);
            }
            if (message.getText().equals("Информация о группе")) {
                info(update);
            }
            if (message.getText().equals("Информация о преподавателе")) {
                info(update);
            }

            if (message.getText().equals("Настройки аккаунта")) {
                accountSettings(update);
            }

        } else if (update.hasCallbackQuery()) {
            String call_data = update.getCallbackQuery().getData();

            if (call_data.equals("all_pinned")) {
                EditMessageText new_message = new EditMessageText()
                        .setChatId(update.getCallbackQuery().getMessage().getChatId())
                        .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                        .setText("Нажмите на кнопку, чтобы получить нужное сообщение. \n " +
                                "Тут будут текстом сообщения с нумерацией и началом сообщения(100 символов) и просьбой отправить нужный номер.");
                try {
                    editMessageText(new_message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            if (call_data.equals("create_pinned")) {
                if (pinned.getMessageId() == 0) {
                    pinned_message_processing = true;
                    sendMsgToPrivate(update, "Перешлите мне сообщение");
                }
            }

            if (call_data.equals("create_ht")) {
                user = databaseConnector.getUserByUserName(update.getCallbackQuery().getFrom().getUserName());
                AnswerCallbackQuery graph = new AnswerCallbackQuery();
                graph.setCallbackQueryId(update.getCallbackQuery().getId());
                graph.setUrl(utilities.authorization(user.getToken()));
                graph.setText("YourURL");
                try {
                    answerCallbackQuery(graph);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            if (call_data.equals("all_ht")) {
                user = databaseConnector.getUserByUserName(update.getCallbackQuery().getFrom().getUserName());
                PageList pageList = null;
                try {
                    pageList = new GetPageList(user.getToken())
                            .setLimit(10)
                            .execute();
                } catch (TelegraphException e) {
                    e.printStackTrace();
                }
            }
            if (call_data.equals("info")) {
                EditMessageText new_message = new EditMessageText()
                        .setChatId(update.getCallbackQuery().getMessage().getChatId())
                        .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                        .setText("Здесь вы можете добавить информацию о себе");
                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> namePr = new ArrayList<>();
                List<InlineKeyboardButton> phoneP = new ArrayList<>();
                List<InlineKeyboardButton> mailPro = new ArrayList<>();
                List<InlineKeyboardButton> newEnt = new ArrayList<>();
                namePr.add(new InlineKeyboardButton().setText("Добавить ФИО").setCallbackData("low"));
                phoneP.add(new InlineKeyboardButton().setText("Добавить Телефон преподавателя").setCallbackData("low"));
                mailPro.add(new InlineKeyboardButton().setText("Почта или что удобно").setCallbackData("low"));
                newEnt.add(new InlineKeyboardButton().setText("Новый преподаватель/Организация").setCallbackData("low"));
                rowsInline.add(namePr);
                rowsInline.add(phoneP);
                rowsInline.add(mailPro);
                rowsInline.add(newEnt);
                markupInline.setKeyboard(rowsInline);
                new_message.setReplyMarkup(markupInline);

                try {
                    editMessageText(new_message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            if (call_data.equals("authorization")) {
                System.out.println(user);
                User root = databaseConnector.getUserByUserName(update.getCallbackQuery().getFrom().getUserName());
                AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
                answerCallbackQuery.setUrl(utilities.authorization((String) databaseConnector.getParamByUserName(root.getUserName(), "token")));
                answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());
                answerCallbackQuery.setText("sa");

                try {
                    answerCallbackQuery(answerCallbackQuery);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                accountSettings(update);
            }

        }
    }

    private void info(Update update) {
        user = databaseConnector.getUserByUserName(update.getMessage().getFrom().getUserName());
        SendMessage sendMessage = new SendMessage();
        if (user.getisAdmin()) {
            if ((utilities.getInfo(user.getUserName())).isEmpty()) {
                sendMessage.setText("К сожалению, ваши ученики пока не присоединились к боту");
            } else {
                sendMessage.setText("Ваши ученики, что подключились к боту: \n" + utilities.getInfo(user.getUserName()));
            }
            sendMessage.setChatId(String.valueOf(user.getUserId())).enableHtml(true);
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            List<InlineKeyboardButton> refUrl = new ArrayList<>();
            refUrl.add(new InlineKeyboardButton().setText("Отправить реферальную ссылку").setSwitchInlineQuery("rf"));
            rowsInline.add(refUrl);
            markupInline.setKeyboard(rowsInline);
            sendMessage.setReplyMarkup(markupInline);
            try {
                sendMessage(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            //сюда
        }
    }

    private void savePin(Update update) {
        pinned_message_processing = false;
        pinned.setFromChatId(update.getMessage().getFrom().getId());
        pinned.setUserName(update.getMessage().getFrom().getUserName());
        pinned.setMessageId(update.getMessage().getMessageId());
        pinned.setTextMessage(update.getMessage().getText());
        try {
            pinned.setForwardFrom(update.getMessage().getForwardFromChat().getTitle());
        } catch (Exception e) {
            pinned.setForwardFrom(update.getMessage().getForwardFrom().getUserName() + " (" + update.getMessage().getForwardFrom().getFirstName() + " " + update.getMessage().getForwardFrom().getLastName() + ")");
        }

        databaseConnector.insert(pinned);

        sendMsgToPrivate(update, "Готово, теперь вы можете его получить из всех закрепленных.");
        pinned.reset();
    }

    private void handleIncomingInlineQuery(InlineQuery inlineQuery) {
        String query = inlineQuery.getQuery().trim().toLowerCase();
        if (query.equals("ht".trim().toLowerCase())) {
            try {
                answerInlineQuery(utilities.converteResultsToResponse(inlineQuery, inlineQuery.getFrom().getUserName(), "ht"));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        if (query.equals("pm".trim().toLowerCase())) {
            try {
                answerInlineQuery(utilities.converteResultsToResponse(inlineQuery, inlineQuery.getFrom().getUserName(), "pm"));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        if (query.equals("rf".trim().toLowerCase())) {
            try {
                answerInlineQuery(utilities.converteResultsToResponse(inlineQuery, String.valueOf(inlineQuery.getFrom().getId()), "rf"));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void homeTask(Update update) {
        user = databaseConnector.getUserByUserName(update.getMessage().getFrom().getUserName());
        SendMessage sendMessage = new SendMessage().setChatId(String.valueOf(user.getUserId()));
        //оптимизировать
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        if (user.getisAdmin()) {
            rowInline.add(new InlineKeyboardButton().setText("Создать новое").setCallbackData("create_ht"));
            rowInline.add(new InlineKeyboardButton().setText("Показать все Домашние задания").setCallbackData("all_ht"));
            sendMessage.setText("Это меню управления домашними заданиями");
        } else {
            rowInline.add(new InlineKeyboardButton().setText("Показать все Домашние задания").setCallbackData("all_ht"));
            sendMessage.setText("Тут вы можете посмотреть все Ваши домашние задания");
        }
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        sendMessage.setReplyMarkup(markupInline);
        user.reset();
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sub_markup(Update update) {
        SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId()).setText("Здесь вы можете просматривать все домашние задания, мероприятия или сохраненные сообщения"); // Create a message object object
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Домашние задания");
        row.add("Сохраненные сообщения");
        keyboard.add(row);
        row = new KeyboardRow();
        row.add("Информация о преподавателе");
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        message.setReplyMarkup(keyboardMarkup);
        try {
            sendMessage(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void subber(Update update, int userId) {
        user.setUserName(update.getMessage().getFrom().getUserName());
        user.setAuthorName(databaseConnector.getParamByUserId(Integer.parseInt(String.valueOf(userId)), "userName").toString());
        user.setToken(String.valueOf(databaseConnector.getParamByUserId(userId, "token")));
        user.setisAdmin(false);
        user.setUserId(update.getMessage().getFrom().getId());
        databaseConnector.insert(user);
        user.reset();
    }

    private void pinned(Update update) {
        user = databaseConnector.getUserByUserName(update.getMessage().getFrom().getUserName());
        SendMessage sendMessage = new SendMessage().setChatId(String.valueOf(user.getUserId()));
        //оптимизировать
        InlineKeyboardMarkup buttons = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> lines = new ArrayList<>();
        List<InlineKeyboardButton> line = new ArrayList<>();
        if (user.getisAdmin()) {
            line.add(new InlineKeyboardButton().setText("Добавить новое").setCallbackData("create_pinned"));
            sendMessage.setText("Это меню управления Закрепленными сообщениями");
        } else {
            sendMessage.setText("Тут вы можете посмотреть все Ваши домашние задания");
        }
        line.add(new InlineKeyboardButton().setText("Все закрепленные").setCallbackData("all_pinned"));
        lines.add(line);
        buttons.setKeyboard(lines);
        sendMessage.setReplyMarkup(buttons);
        user.reset();
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void accountSettings(Update update) {
        if (update.hasMessage()) {
            user.setUserName(update.getMessage().getFrom().getUserName());
            user.setUserId(update.getMessage().getFrom().getId());
        } else {
            user.setUserName(update.getCallbackQuery().getFrom().getUserName());
            user.setUserId(update.getCallbackQuery().getFrom().getId());
        }

        if (databaseConnector.exists(user.getUserId())) {
            SendMessage new_message = new SendMessage()
                    .setChatId(String.valueOf(databaseConnector.getParamByUserName(user.getUserName(), "userId")))
                    .setText("Вы вошли как " + user.getUserName() + ". Это меню настроек аккаунта");
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            List<InlineKeyboardButton> auth = new ArrayList<>();
            List<InlineKeyboardButton> profileLink = new ArrayList<>();
            List<InlineKeyboardButton> createPage = new ArrayList<>();
            List<InlineKeyboardButton> singUp = new ArrayList<>();
            List<InlineKeyboardButton> info = new ArrayList<>();
            auth.add(new InlineKeyboardButton().setText("Edit Author Name").setCallbackData("low"));
            profileLink.add(new InlineKeyboardButton().setText("Edit Profile Link").setCallbackData("low"));
            createPage.add(new InlineKeyboardButton().setText("Edit Account Name").setCallbackData("low"));
            singUp.add(new InlineKeyboardButton().setText("Sing Up at this devise").setCallbackData("low"));
            info.add(new InlineKeyboardButton().setText("Добавить информацию о себе").setCallbackData("info"));
            rowsInline.add(auth);
            rowsInline.add(profileLink);
            rowsInline.add(createPage);
            rowsInline.add(singUp);
            rowsInline.add(info);
            markupInline.setKeyboard(rowsInline);
            new_message.setReplyMarkup(markupInline);
            user.reset();
            try {
                sendMessage(new_message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            createAccount(update);
        }

    }

    private String saveText(Update update, String userName) {
        String root_text = null;
        if (userName.equals(user.getUserName())) {
            root_text = update.getMessage().getText();
        }
        assert root_text != null;
        if (root_text.equals("Настройки аккаунта")) {
            return null;
        }
        return root_text;
    }

    private void createAccount(Update update) {
        create_account_processing = true;

        if (user.getAuthorName() != null) {
            try {
                Account graphAcc = new CreateAccount(user.getUserName())
                        .setAuthorName(user.getAuthorName())
                        .setAuthorUrl("https://telegram.me/" + user.getUserName())
                        .execute();
                user.setToken(graphAcc.getAccessToken());
            } catch (TelegraphException e) {
                e.printStackTrace();
            }
            SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId()).setText("Отлично! Аккаунт создан. Вот ваша ссылка для авторизации этого аккаунта");
            user.setisAdmin(true);
            user.setUserId(update.getMessage().getFrom().getId());
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            List<InlineKeyboardButton> rowline = new ArrayList<>();
            rowline.add(new InlineKeyboardButton().setText("Войти как " + user.getUserName()).setCallbackData("authorization"));
            databaseConnector.insert(user);
            user.reset();
            rowsInline.add(rowline);
            markupInline.setKeyboard(rowsInline);
            message.setReplyMarkup(markupInline);
            create_account_processing = false;
            try {
                sendMessage(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (user.getAuthorName() == null) {
            sendMsgToPrivate(update, "Введите authorName");
        }
    }

    private void markup(Update update, String string) {
        SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId()).setText(string);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Домашние задания");
        row.add("Настройки аккаунта");
        keyboard.add(row);
        row = new KeyboardRow();
        row.add("Сохраненные сообщения");
        row.add("Информация о группе");
        keyboard.add(row);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);
        try {
            sendMessage(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMsgToPrivate(Update update, String text) {
        int id = 0;
        SendMessage sendMessage = new SendMessage();
        if (update.hasCallbackQuery()) {
            id = update.getCallbackQuery().getFrom().getId();
        } else if (update.getMessage().hasText()) {
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
}
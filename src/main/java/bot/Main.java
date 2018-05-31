package bot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegraph.ExecutorOptions;
import org.telegram.telegraph.TelegraphContext;
import org.telegram.telegraph.TelegraphContextInitializer;

public class Main {
    static {
        ApiContextInitializer.init();
        TelegraphContextInitializer.init();
        TelegraphContext.registerInstance(ExecutorOptions.class, new ExecutorOptions());
    }
    public static void main(String[] args) {
        System.out.println("Telegraph: started");
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
            telegramBotsApi.registerBot(new TelegramBot());
            System.out.println("Bot: started");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
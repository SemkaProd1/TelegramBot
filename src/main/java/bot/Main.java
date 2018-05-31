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
    }
    public static void main(String[] args) {

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        TelegraphContext.registerInstance(ExecutorOptions.class, new ExecutorOptions());
        System.out.println("Telegraph: started");
        try {
            telegramBotsApi.registerBot(new TelBot());
            System.out.println("Bot: started");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
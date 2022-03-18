package com.company;

import com.company.controller.BotControl;
import com.company.util.FileJson;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.*;

public class Main {

    public static BotControl MY_TELEGRAM_BOT;

    public static String path = "src/main/resources/files/users.json";
    public static File file = new File(path);
    public static File fileAdmin = new File("src/main/resources/files/admin.json");
    public static File fileBook = new File("src/main/resources/files/books.json");
    public static File fileContact = new File("src/main/resources/files/contacts.json");
    public static File fileTwilio = new File("src/main/resources/files/twilioCode.json");
    public static File fileCategory = new File("src/main/resources/files/category.json");
    public static File fileAllBook = new File("src/main/resources/allBook.xlsx");


    public static void main(String[] args) {

        FileJson.readFromUserJson();
        FileJson.readFromBooksJson();
        FileJson.readFromCategoryJson();
        FileJson.readAdminJson();
        FileJson.readContactMap();
        FileJson.readFromTwilioCode();

        Main.MY_TELEGRAM_BOT = new BotControl();
        headerMethod();
    }

    private static void headerMethod() {

        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(MY_TELEGRAM_BOT);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
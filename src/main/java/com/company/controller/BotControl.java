package com.company.controller;

import com.company.database.Database;
import com.company.service.BotService;
import com.company.util.FileJson;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class BotControl extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "buybookbybot";
    }

    @Override
    public String getBotToken() {
        return "5228837595:AAEJ2ijhy71lhmeBdjv8LBDEb7tlnI9-1Zc";
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {

            Message message = update.getMessage();

            System.out.println(message.getFrom() + ": " + message.getText());

            if (Database.admin.getId().equals(String.valueOf(message.getFrom().getId()))) {

                AdminController adminController = new AdminController(message);
                adminController.start();

            } else if (Database.users.stream()
                    .anyMatch(user -> user.getId().equals(String.valueOf(message.getFrom().getId())))) {

                UserController userController = new UserController(message);
                userController.start();

            } else {

                BotService botService = new BotService(message);
                botService.start();

            }

        } else if (update.hasCallbackQuery()) {

            CallbackQuery callbackQuery = update.getCallbackQuery();
            QueryController queryController = new QueryController(callbackQuery);
                    queryController.start();

        }
    }

    public void sendMsg(SendMessage sendMessage) {

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public void sendMsg(DeleteMessage deleteMessage) {

        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
    public void sendMsg(SendPhoto sendPhoto) {

        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
    public void sendMsg(SendDocument sendDocument) {

        try {
            execute(sendDocument);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
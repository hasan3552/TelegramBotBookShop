package com.company.controller;

import com.company.Main;
import com.company.database.Database;
import com.company.model.User;
import com.company.service.LocationService;
import com.company.service.UserService;
import com.company.util.DemoUtil;
import com.company.util.KeyboardMarkupUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class UserController extends Thread {

    private Message message;

    public UserController(Message message) {
        this.message = message;
    }

    @Override
    public void run() {

        String idStr = String.valueOf(message.getFrom().getId());
        User currentUser = null;
        for (User user : Database.users) {
            if (user.getId().equals(idStr)) {
                currentUser = user;
                break;
            }
        }
        UserService userService = new UserService(currentUser,idStr);

        if (message.hasText()) {

            if (message.getText().equals(DemoUtil.BUY_BOOK_UZ) || message.getText().equals(DemoUtil.BUY_BOOK_RU)) {
                userService.buyBook(message);
            } else if (message.getText().equals(DemoUtil.SALE_HISTORY_UZ) || message.getText().equals(DemoUtil.SALE_HISTORY_RU)) {
                userService.start();
            } else if (message.getText().equals(DemoUtil.SETTING_UZ) || message.getText().equals(DemoUtil.SETTING_RU)) {
                userService.setting(message);
            } else if (message.getText().equals(DemoUtil.BASKET_UZ) || message.getText().equals(DemoUtil.BASKET_RU)) {
                userService.basket(currentUser, idStr);
            } else {

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(message.getChatId()));
                sendMessage.setReplyMarkup(KeyboardMarkupUtil.getUserMenu(currentUser.getLanguage()));
                sendMessage.setText(message.getText());
                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
            }

        } else if (message.hasLocation()) {
            LocationService.main(currentUser, idStr, message.getLocation());
        }
    }
}

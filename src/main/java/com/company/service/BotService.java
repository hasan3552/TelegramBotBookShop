package com.company.service;

import com.company.Main;
import com.company.database.Database;
import com.company.enums.Language;
import com.company.model.User;
import com.company.util.FileJson;
import com.company.util.KeyboardMarkupUtil;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public class BotService extends Thread {

    private Message message;

    public BotService(Message message) {
        this.message = message;
    }

    @Override
    public void run() {
        SendMessage sendMessage = new SendMessage();

        if (message.hasContact()) {

           // System.out.println("message.getContact().getPhoneNumber() = " + message.getContact
            // ().getPhoneNumber());
            TwilioCode twilioCode = new TwilioCode(message.getContact().getPhoneNumber(), message.getChatId());
            twilioCode.start();

            Database.contactMap.put(message.getChatId(), message.getContact());
            FileJson.writeContactMap(Database.contactMap);
//            try {
//                twilioCode.join(200000);
//                Database.twilioCode.remove(message.getChatId());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            sendMessage.setChatId(String.valueOf(message.getChatId()));
            sendMessage.setText("Please send the code sent via SMS to your phone number.");

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (message.hasText()) {

            if (message.getText().equals("/start")) {

                sendMessage.setText("Please enter contact");
                sendMessage.setChatId(String.valueOf(message.getChatId()));
                ReplyKeyboardMarkup contact = KeyboardMarkupUtil.getContact();
                sendMessage.setReplyMarkup(contact);
                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

            } else if (Database.twilioCode.containsKey(message.getChatId())) {

                sendMessage.setChatId(String.valueOf(message.getChatId()));
                if (String.valueOf(Database.twilioCode.get(message.getChatId())).equals(message.getText())) {

                    User user = new User(String.valueOf(message.getFrom().getId()),
                            message.getFrom().getFirstName(), message.getFrom().getLastName(),
                            Database.contactMap.get(message.getChatId()).getPhoneNumber(),
                            message.getFrom().getUserName());

                    FileJson.writeToUserJson(user);

                    sendMessage.setText("Successfully register!");
                    Database.twilioCode.remove(message.getChatId());

                    sendMessage.setReplyMarkup(KeyboardMarkupUtil.getUserMenu(Language.UZ));
                    Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
                } else {
                    sendMessage.setText("Error");
                    Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
                }
            }
        }
    }
}
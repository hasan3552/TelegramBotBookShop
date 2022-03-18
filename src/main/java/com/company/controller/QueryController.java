package com.company.controller;

import com.company.Main;
import com.company.database.Database;
import com.company.enums.BookType;
import com.company.enums.Language;
import com.company.model.Category;
import com.company.model.User;
import com.company.service.AdminService;
import com.company.service.UserService;
import com.company.util.DemoUtil;
import com.company.util.FileJson;
import com.company.util.InlineKeyboardUtil;
import com.company.util.KeyboardMarkupUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public class QueryController extends Thread {

    private CallbackQuery callbackQuery;

    public QueryController(CallbackQuery callbackQuery) {
        this.callbackQuery = callbackQuery;
    }

    @Override
    public void run() {

        SendMessage sendMessage = new SendMessage();
        String data = callbackQuery.getData();
        String idStr = String.valueOf(callbackQuery.getFrom().getId());
        User user1 = null;

        for (User user : Database.users) {
            if (user.getId().equals(idStr)) {
                user1 = user;
                break;
            }
        }
        if (user1 == null) {

            AdminService adminService = new AdminService(callbackQuery.getMessage());
            adminService.workCallbackQuery(callbackQuery);

        } else {
            Language language = user1.getLanguage();

            if (data.equals("change_language")) {

                sendMessage.setChatId(idStr);
                sendMessage.setText(language.equals(Language.UZ) ? "Язык успешно изменен." : "Til muvvoffaqiyatli " +
                        "o'zgartilirdi.");

                user1.setLanguage(language.equals(Language.UZ) ? Language.RU : Language.UZ);
                FileJson.writeToUserJson(Database.users);

                sendMessage.setReplyMarkup(KeyboardMarkupUtil.getUserMenu(user1.getLanguage()));
                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

            } else if (data.equals("don't_change_language")) {

                sendMessage.setChatId(idStr);
                sendMessage.setText(!language.equals(Language.UZ) ? "Язык остался без изменений." :
                        "Til o'zgarishsiz qoldirildi.");
                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

            } else if (data.equals("_buyBook")) {
                UserService userService = new UserService(user1, user1.getId());
                userService.buyBook(callbackQuery.getMessage());
            }
            else if (data.equals(DemoUtil.CHANGE_PRINT_BOOKS) || data.equals(DemoUtil.CHANGE_AUDIO_BOOKS)
                    || data.equals(DemoUtil.CHANGE_ELECTRON_BOOKS)) {

                InlineKeyboardMarkup categoryBooks = InlineKeyboardUtil.getCategoryBooks(language, data);
                sendMessage.setReplyMarkup(categoryBooks);
                sendMessage.setText(language.equals(Language.UZ) ? "Kitobning kategoriyasini " +
                        "tanlang" : "Выберите категорию книги");
                sendMessage.setChatId(idStr);
                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

            }

           else if ( data.contains(DemoUtil.CHANGE_AUDIO_BOOKS)){

                Category category1 = null;
                for (Category category : Database.categories) {
                    if (data.contains(category.getNameUz())){
                        category1 = category;
                        break;
                    }
                }

                BookType bookType = BookType.Audio_Book;
                InlineKeyboardMarkup bookForInline = InlineKeyboardUtil.getBookForInline(language, category1, bookType);
                sendMessage.setChatId(idStr);
                sendMessage.setText(language.equals(Language.UZ) ? "Audio kitobni tanlang" :
                        "Выбрать аудиокнигу");
                sendMessage.setReplyMarkup(bookForInline);
                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
            }
            else if (data.contains(DemoUtil.CHANGE_PRINT_BOOKS)){
                Category category1 = null;
                for (Category category : Database.categories) {
                    if (data.contains(category.getNameUz())){
                        category1 = category;
                        break;
                    }
                }
                InlineKeyboardMarkup bookForInline = InlineKeyboardUtil.getBookForInline(
                        language, category1, BookType.Printer_Book);

                sendMessage.setChatId(idStr);
                sendMessage.setText(language.equals(Language.UZ) ? "Kitobni tanlang" :
                        "Выберите книгу");
                sendMessage.setReplyMarkup(bookForInline);
                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
            }
            else if ( data.contains(DemoUtil.CHANGE_ELECTRON_BOOKS)){

                Category category1 = null;
                for (Category category : Database.categories) {
                    if (data.contains(category.getNameUz())){
                        category1 = category;
                        break;
                    }
                }

                InlineKeyboardMarkup bookForInline = InlineKeyboardUtil.getBookForInline(
                        language, category1, BookType.Electron_Book);
                sendMessage.setChatId(idStr);
                sendMessage.setText(language.equals(Language.UZ) ? "Elektron kitobni tanlang" :
                        "Выберите электронную книгу");
                sendMessage.setReplyMarkup(bookForInline);
                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
            }


            else if (data.equals("clear_basket")) {
                user1.getDetails().clear();
                FileJson.writeToUserJson(Database.users);
                sendMessage.setChatId(idStr);
                sendMessage.setText(language.equals(Language.UZ) ?
                        "Savatingiz tozalandi. " : "Ваша корзина очищена.");
                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

            } else if (data.equals("basket_sale")) {

                sendMessage.setText(language.equals(Language.UZ) ?
                        "Haridingiz qabul qilinishi uchun iltimos joylashuvingizni yuboring." :
                        "Пожалуйста, отправьте свое местоположение, чтобы ваша покупка была принята.");
                ReplyKeyboardMarkup location = KeyboardMarkupUtil.getLocation(language);
                sendMessage.setChatId(idStr);
                sendMessage.setReplyMarkup(location);
                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

            } else {

                UserService userService = new UserService(user1, user1.getId());
                userService.workCallbackQuery(callbackQuery, user1);

            }

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(callbackQuery.getFrom().getId()));
            deleteMessage.setMessageId(callbackQuery.getMessage().getMessageId());
            Main.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

        }
    }
}
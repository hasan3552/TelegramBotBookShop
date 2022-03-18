package com.company.util;

import com.company.database.Database;
import com.company.enums.Language;
import com.company.service.UserService;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class KeyboardMarkupUtil {

    public static ReplyKeyboardMarkup getUserMenu(Language language) {

        //Language language = UserService.currentLanguage(message);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton(
                language.equals(Language.UZ) ? DemoUtil.BUY_BOOK_UZ : DemoUtil.BUY_BOOK_RU);
        keyboardRow1.add(keyboardButton);
        keyboardRows.add(keyboardRow1);

        KeyboardRow keyboardRow2 = new KeyboardRow();
        KeyboardButton keyboardButton1 = new KeyboardButton(
                language.equals(Language.UZ) ? DemoUtil.SALE_HISTORY_UZ : DemoUtil.SALE_HISTORY_RU);
        keyboardRow2.add(keyboardButton1);
        keyboardRows.add(keyboardRow2);

        KeyboardRow keyboardRow3 = new KeyboardRow();
        KeyboardButton keyboardButton2 = new KeyboardButton(
                language.equals(Language.UZ) ? DemoUtil.BASKET_UZ : DemoUtil.BASKET_RU);
        KeyboardButton keyboardButton3 = new KeyboardButton(
                language.equals(Language.UZ) ? DemoUtil.SETTING_UZ : DemoUtil.SETTING_RU);
        keyboardRow3.add(keyboardButton2);
        keyboardRow3.add(keyboardButton3);
        keyboardRows.add(keyboardRow3);

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup getContact() {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton("\uD83D\uDCDE Contact");
        keyboardButton.setRequestContact(true);

        keyboardRow1.add(keyboardButton);
        keyboardRows.add(keyboardRow1);

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup getAdminMenu(Language language) {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        replyKeyboardMarkup.setResizeKeyboard(true);

        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardButton keyboardButton1 = new KeyboardButton(
                language.equals(Language.UZ) ? DemoUtil.USERS_UZ : DemoUtil.USERS_RU);
        KeyboardButton keyboardButton2 = new KeyboardButton(
                language.equals(Language.UZ) ? DemoUtil.ORDERS_UZ : DemoUtil.ORDERS_RU);

        keyboardRow1.add(keyboardButton1);
        keyboardRow1.add(keyboardButton2);
        keyboardRows.add(keyboardRow1);

        KeyboardRow keyboardRow2 = new KeyboardRow();
        KeyboardButton keyboardButton3 = new KeyboardButton(
                language.equals(Language.UZ) ? DemoUtil.BOOK_CRUD_UZ : DemoUtil.BOOK_CRUD_RU);
        keyboardRow2.add(keyboardButton3);
        KeyboardButton button1 = new KeyboardButton(Database.admin.getLanguage().equals(Language.UZ) ?
                       DemoUtil.CATEGORY_CRUD_UZ : DemoUtil.CATEGORY_CRUD_RU );
        keyboardRow2.add(button1);
        keyboardRows.add(keyboardRow2);

        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton(Database.admin.getLanguage().equals(Language.UZ) ?
                DemoUtil.SETTING_UZ : DemoUtil.SETTING_RU);
        row.add(button);
        keyboardRows.add(row);

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup getLocation(Language language) {

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton(language.equals(Language.UZ) ?
                "\uD83D\uDCCC JOYLASHUV" : "\uD83D\uDCCC МЕСТО НАХОЖДЕНИЯ");
        button.setRequestLocation(true);
        row.add(button);
        rows.add(row);
        markup.setKeyboard(rows);
        markup.setResizeKeyboard(true);
        return markup;
    }
}

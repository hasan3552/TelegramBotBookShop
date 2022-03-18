package com.company.service;

import com.company.Main;
import com.company.database.Database;
import com.company.enums.CategoryStatus;
import com.company.enums.Language;
import com.company.model.Category;
import com.company.util.FileJson;
import com.company.util.InlineKeyboardUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.Optional;

public class CategoryService {
    public static SendMessage sendMessage = new SendMessage();

    public static void crud() {

        sendMessage.setChatId(Database.admin.getId());
        sendMessage.setText(Database.admin.getLanguage().equals(Language.UZ) ?
                "Categoriya CRUD oynasi." : "Окно категории CRUD.");

        InlineKeyboardMarkup categoryCRUD = InlineKeyboardUtil.getCategoryCRUD(Database.admin.getLanguage());
        sendMessage.setReplyMarkup(categoryCRUD);

        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
    }

    public static SendMessage show(Language language) {

        String text = "";
        text += ((language.equals(Language.UZ)) ? "Kitob kategoriyalari:\n\n" :
                "Категории книг:\n\n");
        for (Category category : Database.categories) {
            if (!category.getIsDelete()) {
                if (language.equals(Language.UZ)) {
                    text += "id: " + category.getId() + "  nomi:" + category.getNameUz() + "\n";

                } else {
                    text += "id: " + category.getId() + "  имя:" + category.getNameRu() + "\n";
                }
            }
        }

        sendMessage.setChatId(Database.admin.getId());
        sendMessage.setText(text);

        return sendMessage;
    }

    public static void create(String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(Database.admin.getId());

        try {
            String[] split = text.split("/");

            Optional<Category> optional = Database.categories.stream()
                    .filter(category -> category.getStatus().equals(CategoryStatus.NEW))
                    .findAny();

            optional.get().setNameUz(split[1]);
            optional.get().setNameRu(split[2]);
            optional.get().setStatus(CategoryStatus.CREATE);
            FileJson.writeToCaregoryJson(Database.categories);

            sendMessage.setText(Database.admin.getLanguage().equals(Language.UZ) ?
                    "Categoriya muvvoffaqiyatli qo'shildi." : "Категория успешно добавлена.");
        } catch (Exception e) {
            sendMessage.setText(Database.admin.getLanguage().equals(Language.UZ) ?
                    "Categoriya qo'shishda xatolik yuz berdi .\n" +
                            "Iltimos qaytadan urunib ko'ring." :
                    "Произошла ошибка при добавлении категории.\n" +
                            "Пожалуйста, попробуйте еще раз.");
        }
        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

    }

    public static void update(String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(Database.admin.getId());
        try {
            String[] split = text.split("/");
            int i = Integer.parseInt(split[1]);

            Category category1 = null;
            for (Category category : Database.categories) {
                if (category.getId() == i) {
                    category1 = category;
                }
            }

            category1.setNameUz(split[2]);
            category1.setNameRu(split[3]);
            FileJson.writeToCaregoryJson(Database.categories);
            sendMessage.setText(Database.admin.getLanguage().equals(Language.UZ) ?
                    "Kategoriya o'zgartirildi." : "Категория изменена.");

        } catch (Exception e) {

            sendMessage.setText(Database.admin.getLanguage().equals(Language.UZ) ?
                    "Kategoriyani o'zgartirishda xatolik yuz berdi. Iltimos qaytadan urinib " +
                            "ko'ring." :
                    "При смене категории произошла ошибка. Пожалуйста, попробуйте еще раз.");
        }

        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
    }

    public static void delete(String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(Database.admin.getId());
        try {
            String[] split = text.split("/");
            int i = Integer.parseInt(split[1]);

            if (Database.categories.size()<i){
                int i1 = i / 0;
            }

            for (Category category : Database.categories) {
                if (category.getId().equals(i)) {
                    category.setIsDelete(true);
                    break;
                }
            }

            FileJson.writeToCaregoryJson(Database.categories);
            sendMessage.setText(Database.admin.getLanguage().equals(Language.UZ) ?
                    "Kategoriya o'chirildi." : "Категория удалена.");

        } catch (Exception e) {
            sendMessage.setText(Database.admin.getLanguage().equals(Language.UZ) ?
                    "Kategoriyani o'zgartirishda xatolik yuz berdi. Iltimos qaytadan urinib " +
                            "ko'ring" : "Произошла ошибка при изменении категории." +
                    " Пожалуйста, попробуйте еще раз");
        }
        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
    }
}

package com.company.service;

import com.company.Main;
import com.company.database.Database;
import com.company.enums.Language;
import com.company.model.Detail;
import com.company.model.User;
import com.company.util.FileJson;
import com.company.util.KeyboardMarkupUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;

import java.util.List;

public class LocationService {

    public static void main(User user, String id, Location location) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(id);
        sendMessage.setText(user.getLanguage().equals(Language.UZ) ? "" +
                "Haridingiz qabul qilindi.Yetkazib berish muddati 1-3 kun.Agarda sizning " +
                "haridlar savatchangizda audio yoki elektron kitoblar ham mavjud bo'lsa siz " +
                "kiritgan kantakt orqali sizga aloqaga chiqamiz. Haridingiz uchun tashakkur." :
                "Ваша покупка принята Срок доставки 1-3 дня Спасибо за покупку.Если в вашей корзине" +
                " также есть аудио- или электронные книги, мы свяжемся с вами по указанному вами" +
                        " контакту.");
        sendMessage.setReplyMarkup(KeyboardMarkupUtil.getUserMenu(user.getLanguage()));
        List<Detail> details = user.getDetails();
        user.addToHistorySale(details, location);
        user.getDetails().clear();

        FileJson.writeToUserJson(Database.users);
        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
    }

}

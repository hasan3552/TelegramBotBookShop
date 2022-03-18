package com.company.util;

import java.io.File;

public interface DemoUtil {

    File userJsonFile = new File("src/main/resources/files/users.json");

    String BUY_BOOK_UZ = "\uD83D\uDCDA KITOB SOTIB OLISH";
    String BUY_BOOK_RU = "\uD83D\uDCDA КУПИТЬ КНИГУ";

    String SALE_HISTORY_UZ =  "\uD83D\uDCC3 XARIDLAR TARIXI";
    String SALE_HISTORY_RU = "\uD83D\uDCC3 ИСТОРИЯ ПОКУПОК";

    String BASKET_UZ = "🛒 SAVATCHA";
    String BASKET_RU = "🛒 КОРЗИНА";

    String SETTING_UZ = "⚙️ SOZLAMALAR";
    String SETTING_RU = "⚙️ НАСТРОЙКИ";

    String WONT_SETTING_LAN_UZ = "Tilni o'zgartirishni xoxlaysizmi? ";
    String WONT_SETTING_LAN_RU = "Вы хотите изменить язык? ";

    String USERS_UZ = "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83D\uDCBB FOYDALANUVCHILAR";
    String USERS_RU = "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83D\uDCBB ПОЛЬЗОВАТЕЛИ";

    String ORDERS_UZ = "\uD83D\uDE9B BUYURTMALAR";
    String ORDERS_RU = "\uD83D\uDE9B ЗАКАЗЫ";

    String BOOK_CRUD_UZ = "\uD83D\uDCD3 KITOBLAR CRUD";
    String BOOK_CRUD_RU = "\uD83D\uDCD3 КНИГИ CRUD";

    String CATEGORY_CRUD_UZ = "\uD83D\uDCCA KATEGORIYA CRUD";
    String CATEGORY_CRUD_RU = "\uD83D\uDCCA КАТЕГОРИЯ CRUD";

    String ORDER_STATUS_CRUD_UZ = "\uD83E\uDDFE  BUYURTMA STATUSI CRUD";
    String ORDER_STATUS_CRUD_RU = "\uD83E\uDDFE  СТАТУС ЗАКАЗА CRUD";

    String ORDER_STATUS_UZ = "\uD83D\uDD79  BUYURTMA STATUSI O'ZGARTIRISH";
    String ORDER_STATUS_RU = "\uD83D\uDD79  СТАТУС ЗАКАЗА ИЗМЕНЯТЬ";

    String AUDIO_BOOKS_UZ = "\uD83C\uDFA7 AUDIO KITOBLAR";
    String AUDIO_BOOKS_RU = "\uD83C\uDFA7 АУДИО КНИГИ";

    String PRINT_BOOKS_UZ = "\uD83D\uDCD4 BOSMA KITOBLAR";
    String PRINT_BOOKS_RU = "\uD83D\uDCD4 ИЗДАННЫЕ КНИГИ";

    String ELECTRON_BOOKS_UZ = "\uD83D\uDCC2 ELEKTRON KITOBLAR";
    String ELECTRON_BOOKS_RU = "\uD83D\uDCC2 ЭЛЕКТРОННЫЕ КНИГИ";

    String CHANGE_AUDIO_BOOKS = "change_audio_books";
    String CHANGE_PRINT_BOOKS = "change_print_books";
    String CHANGE_ELECTRON_BOOKS = "change_electron_books";

    String CHANGE_AUDIO_BOOKS_DELETE = "change_audio_books_delete";
    String CHANGE_PRINT_BOOKS_DELETE = "change_print_books_delete";
    String CHANGE_ELECTRON_BOOKS_DELETE = "change_electron_books_delete";

    String CHANGE_CATEGORY_SHOW = "_category_show";

    String SHOW_ALL_BOOKS = "_show_books";
    String CREATE_BOOK = "_create_book";
    String DELETE_BOOK = "_delete_book";
    String UPDATE_BOOK = "_updete_book";

    String CHANGE_CATEGORY_CREATE = "category_create";
    String CHANGE_CATEGORY_UPDATE = "category_update";
    String CHANGE_CATEGORY_DELETE = "category_delete";

    String UPDATE_AUDIO = "_update_audio";
    String UPDATE_EBOOK = "_update_ebook";
    String UPDATE_PBOOK = "_update_pbook";

    String UPDATE_NEW_NAME_AUTHOR = "_update_new_name_author";
    String UPDATE_NEW_PRICE = "_update_new_price";
    String UPDATE_NEW_PHOTO = "_update_new_photo";
    String UPDATE_NEW_BLOCK = "_update_new_block";
}

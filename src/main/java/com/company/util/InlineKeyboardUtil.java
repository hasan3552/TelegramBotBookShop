package com.company.util;

import com.company.database.Database;
import com.company.enums.BookType;
import com.company.enums.CategoryStatus;
import com.company.enums.Language;
import com.company.enums.StatusBook;
import com.company.model.Book;
import com.company.model.Category;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InlineKeyboardUtil {


    public static InlineKeyboardMarkup getChangeLanguage() {
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();


        button.setText("✅");
        button.setCallbackData("change_language");
        row.add(button);
        button1.setText("❌");
        button1.setCallbackData("don't_change_language");
        row.add(button1);
        rowList.add(row);
        markup.setKeyboard(rowList);

        return markup;
    }

    public static InlineKeyboardMarkup getBookType(Language language) {

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();


        button.setText(language.equals(Language.UZ) ? DemoUtil.AUDIO_BOOKS_UZ : DemoUtil.AUDIO_BOOKS_RU);
        button.setCallbackData(DemoUtil.CHANGE_AUDIO_BOOKS);
        row.add(button);
        rowList.add(row);

        button1.setText(language.equals(Language.UZ) ? DemoUtil.PRINT_BOOKS_UZ : DemoUtil.PRINT_BOOKS_RU);
        button1.setCallbackData(DemoUtil.CHANGE_PRINT_BOOKS);
        row1.add(button1);
        rowList.add(row1);

        button2.setText(language.equals(Language.UZ) ? DemoUtil.ELECTRON_BOOKS_UZ : DemoUtil.ELECTRON_BOOKS_RU);
        button2.setCallbackData(DemoUtil.CHANGE_ELECTRON_BOOKS);
        row2.add(button2);
        rowList.add(row2);

        button3.setText(language.equals(Language.UZ) ? "🔁 ORQAGA" :
                "🔁 НАЗАД");
        button3.setCallbackData("_orqaga");
        row3.add(button3);
        rowList.add(row3);

        markup.setKeyboard(rowList);

        return markup;

    }

    public static InlineKeyboardMarkup getCategoryBooks(Language language, String data) {

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        for (Category category : Database.categories) {
            if (category.getStatus().equals(CategoryStatus.CREATE) && !category.getIsDelete()) {

                List<InlineKeyboardButton> row = new ArrayList<>();
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(language.equals(Language.UZ) ? category.getNameUz() : category.getNameRu());
                button.setCallbackData(data + category.getNameUz());
                row.add(button);
                rowList.add(row);
            }
        }

        button1.setText(language.equals(Language.UZ) ?
                "🔁 ORQAGA" : "🔁 НАЗАД");
        button1.setCallbackData("_buyBook");
        row1.add(button1);
        rowList.add(row1);

        markup.setKeyboard(rowList);

        return markup;
    }

    public static InlineKeyboardMarkup getBookForInline(Language language,
                                                        Category category, BookType bookType) {

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        String type = (bookType.equals(BookType.Electron_Book) ? "EBOOK" :
                bookType.equals(BookType.Printer_Book) ? "PBOOK" : "AUDIO");
        for (Book book : Database.books) {

            if (book.getStatusBook().equals(StatusBook.CREATE) &&
                    !book.getCategory().getIsDelete()) {

                if (book.getCategory().getNameUz().equals(category.getNameUz())
                        && book.getBookType().equals(bookType) && !book.getIsBlock()) {


                    List<InlineKeyboardButton> row = new ArrayList<>();
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText((language.equals(Language.UZ) ? "(o'zbek tilida) " : "(на " +
                            "русском) ") + book.getName() + " = " + book.getPrice());
                    button.setCallbackData(book.getName() + type);
                    row.add(button);
                    rowList.add(row);
                }
            }
        }

        button1.setText(language.equals(Language.UZ) ?
                "🔁 ORQAGA" : "🔁 НАЗАД");
        button1.setCallbackData(bookType.equals(BookType.Electron_Book) ? DemoUtil.CHANGE_ELECTRON_BOOKS :
                bookType.equals(BookType.Audio_Book) ? DemoUtil.CHANGE_AUDIO_BOOKS : DemoUtil.CHANGE_PRINT_BOOKS);
        row1.add(button1);
        rowList.add(row1);

        markup.setKeyboard(rowList);
        return markup;

    }

    public static InlineKeyboardMarkup getBasketForInline(Language language) {

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();


        button.setText(language.equals(Language.UZ) ? "✅ SOTIB OLISHNI TASDIQLASH" :
                "✅ ПОДТВЕРЖДЕНИЕ ПОКУПКИ");
        button.setCallbackData("basket_sale");
        row.add(button);
        rowList.add(row);

        button1.setText(language.equals(Language.UZ) ? "❌ SAVATCHANI TOZALASH" :
                "❌ ОЧИСТКА КОРЗИНЫ");
        button1.setCallbackData("clear_basket");
        row1.add(button1);
        rowList.add(row1);

        button2.setText(language.equals(Language.UZ) ? "🔁 ORQAGA" :
                "🔁 НАЗАД");
        button2.setCallbackData("_basket_cansel");
        row2.add(button2);
        rowList.add(row2);

        markup.setKeyboard(rowList);

        return markup;
    }

    public static InlineKeyboardMarkup getCountBook(String data, Language language) {

        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();


        for (int i = 1; i < 10; ) {

            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(String.valueOf(i));
            button.setCallbackData(data + i++);
            row.add(button);

            InlineKeyboardButton button2 = new InlineKeyboardButton();
            button2.setText(String.valueOf(i));
            button2.setCallbackData(data + i++);
            row.add(button2);

            InlineKeyboardButton button3 = new InlineKeyboardButton();
            button3.setText(String.valueOf(i));
            button3.setCallbackData(data + i++);
            row.add(button3);
            rowList.add(row);
        }

        button1.setText(language.equals(Language.UZ) ? "ORQAGA" : "НАЗАД");
        button1.setCallbackData(data + "0");
        row3.add(button1);
        rowList.add(row3);

        markup.setKeyboard(rowList);
        return markup;
    }

    public static InlineKeyboardMarkup getBookCrudForInline(Language language) {

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        InlineKeyboardButton button4 = new InlineKeyboardButton();
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        button.setText(language.equals(Language.UZ) ? "\uD83D\uDCDA Kitoblarni ko'rish" :
                "\uD83D\uDCDA Посмотреть книги");
        button.setCallbackData(DemoUtil.SHOW_ALL_BOOKS);
        row.add(button);
        rowList.add(row);

        button1.setText(language.equals(Language.UZ) ? "➕ Kitob qo'shish" : "➕ Добавить книгу");
        button1.setCallbackData(DemoUtil.CREATE_BOOK);
        row1.add(button1);
        rowList.add(row1);

        button2.setText(language.equals(Language.UZ) ? "\uD83D\uDDD1 Kitob o'chirish" :
                "\uD83D\uDDD1 Удалить книгу");
        button2.setCallbackData(DemoUtil.DELETE_BOOK);
        row2.add(button2);
        rowList.add(row2);

        button3.setText(language.equals(Language.UZ) ? "♻️ O'zgartirish" : "♻️ Изменять");
        button3.setCallbackData(DemoUtil.UPDATE_BOOK);
        row3.add(button3);
        rowList.add(row3);

        button4.setText(language.equals(Language.UZ) ? "🔁 ORQAGA" : "🔁 НАЗАД");
        button4.setCallbackData("_cansel");
        row4.add(button4);
        rowList.add(row4);

        markup.setKeyboard(rowList);
        return markup;

    }

    public static ReplyKeyboard getBookTypeForDelete(Language language) {

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();


        button.setText(language.equals(Language.UZ) ? DemoUtil.AUDIO_BOOKS_UZ : DemoUtil.AUDIO_BOOKS_RU);
        button.setCallbackData(DemoUtil.CHANGE_AUDIO_BOOKS_DELETE);
        row.add(button);
        rowList.add(row);

        button1.setText(language.equals(Language.UZ) ? DemoUtil.PRINT_BOOKS_UZ : DemoUtil.PRINT_BOOKS_RU);
        button1.setCallbackData(DemoUtil.CHANGE_PRINT_BOOKS_DELETE);
        row1.add(button1);
        rowList.add(row1);

        button2.setText(language.equals(Language.UZ) ? DemoUtil.ELECTRON_BOOKS_UZ : DemoUtil.ELECTRON_BOOKS_RU);
        button2.setCallbackData(DemoUtil.CHANGE_ELECTRON_BOOKS_DELETE);
        row2.add(button2);
        rowList.add(row2);

        button3.setText(language.equals(Language.UZ) ? "🔁 ORQAGA" : "🔁 НАЗАД");
        button3.setCallbackData("_orqaga_delete");
        row3.add(button3);
        rowList.add(row3);

        markup.setKeyboard(rowList);

        return markup;
    }

    public static ReplyKeyboard getCategoryBooksForDelete(Language language, String data) {

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();


        for (Category category : Database.categories) {

            if (category.getStatus().equals(CategoryStatus.CREATE)) {

                List<InlineKeyboardButton> row = new ArrayList<>();
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(language.equals(Language.UZ) ? category.getNameUz() : category.getNameRu());
                button.setCallbackData(data + category.getNameUz());
                row.add(button);
                rowList.add(row);

            }
        }

        button1.setText(language.equals(Language.UZ) ? "🔁 ORQAGA" : "🔁 НАЗАД");
        button1.setCallbackData("_delete_booktype");
        row1.add(button1);
        rowList.add(row1);

        markup.setKeyboard(rowList);

        return markup;

    }

    public static InlineKeyboardMarkup getBookForInlineDelete(Language language, Category category,
                                                              BookType bookType) {

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();


        String type = (bookType.equals(BookType.Electron_Book) ? "DELETEEB" :
                bookType.equals(BookType.Printer_Book) ? "DELETEPB" : "DELETEAB");

        for (Book book : Database.books) {

            if (book.getStatusBook().equals(StatusBook.CREATE)) {

                if (book.getCategory().getNameUz().equals(category.getNameUz())
                        && book.getBookType().equals(bookType) && !book.getIsBlock()) {

                    List<InlineKeyboardButton> row = new ArrayList<>();
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText((language.equals(Language.UZ) ? "(o'zbek tilida) " : "(на " +
                            "русском) ") + book.getName() + " = " + book.getPrice());
                    button.setCallbackData(book.getName() + type);
                    row.add(button);
                    rowList.add(row);
                }
            }
        }

        button1.setText(language.equals(Language.UZ) ? "🔁 ORQAGA" : "🔁 НАЗАД");
        button1.setCallbackData(bookType.equals(BookType.Electron_Book) ?
                DemoUtil.CHANGE_ELECTRON_BOOKS_DELETE :
                bookType.equals(BookType.Audio_Book) ? DemoUtil.CHANGE_AUDIO_BOOKS_DELETE :
                        DemoUtil.CHANGE_PRINT_BOOKS_DELETE);
        row1.add(button1);
        rowList.add(row1);

        markup.setKeyboard(rowList);
        return markup;

    }

    public static InlineKeyboardMarkup getCategoryCRUD(Language language) {

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        InlineKeyboardButton button4 = new InlineKeyboardButton();
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();


        button.setText(language.equals(Language.UZ) ? "\uD83D\uDC41\u200D\uD83D\uDDE8 " +
                "KATEGORIYALARNI KO'RISH" : "\uD83D\uDC41\u200D\uD83D\uDDE8 ПОСМОТРЕТЬ КАТЕГОРИИ");
        button.setCallbackData(DemoUtil.CHANGE_CATEGORY_SHOW);
        row.add(button);
        rowList.add(row);

        button1.setText(language.equals(Language.UZ) ? "➕ KATEGORIYA QO'SHISH" :
                "➕ ДОБАВИТЬ КАТЕГОРИЮ");
        button1.setCallbackData(DemoUtil.CHANGE_CATEGORY_CREATE);
        row1.add(button1);
        rowList.add(row1);

        button2.setText(language.equals(Language.UZ) ? "♻ KATEGORIYA O'ZGARTIRISH" :
                "♻ ИЗМЕНЕНИЕ КАТЕГОРИИ");
        button2.setCallbackData(DemoUtil.CHANGE_CATEGORY_UPDATE);
        row2.add(button2);
        rowList.add(row2);

        button3.setText(language.equals(Language.UZ) ? "\uD83D\uDDD1 KATEGORIYA O'CHIRISH." :
                "\uD83D\uDDD1 КАТЕГОРИЯ ВЫКЛ.");
        button3.setCallbackData(DemoUtil.CHANGE_CATEGORY_DELETE);
        row3.add(button3);
        rowList.add(row3);

        button4.setText(language.equals(Language.UZ) ? "\uD83D\uDD01 ORQAGA" :
                "\uD83D\uDD01 НАЗАД");
        button4.setCallbackData("--ORQAGA");
        row4.add(button4);
        rowList.add(row4);

        markup.setKeyboard(rowList);
        return markup;
    }

    public static InlineKeyboardMarkup bookTypeForUpdate(Language language) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>>  rowList = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(language.equals(Language.UZ) ? DemoUtil.AUDIO_BOOKS_UZ : DemoUtil.AUDIO_BOOKS_RU);
        button.setCallbackData(DemoUtil.UPDATE_AUDIO);
        row1.add(button);
        rowList.add(row1);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText(language.equals(Language.UZ) ? DemoUtil.ELECTRON_BOOKS_UZ :
                DemoUtil.ELECTRON_BOOKS_RU);
        button2.setCallbackData(DemoUtil.UPDATE_EBOOK);
        row2.add(button2);
        rowList.add(row2);

        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText(language.equals(Language.UZ) ? DemoUtil.PRINT_BOOKS_UZ :
                DemoUtil.PRINT_BOOKS_RU);
        button3.setCallbackData(DemoUtil.UPDATE_PBOOK);
        row3.add(button3);
        rowList.add(row3);

        List<InlineKeyboardButton> row4 = new ArrayList<>();
        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button4.setText(language.equals(Language.UZ) ? "\uD83D\uDD01 ORQAGA" :
                "\uD83D\uDD01 НАЗАД");
        button4.setCallbackData("__ORQAGA");
        row4.add(button4);
        rowList.add(row4);

        markup.setKeyboard(rowList);

        return markup;
    }

    public static InlineKeyboardMarkup getBookCategoryForUpdate(Language language, BookType bookType) {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (Category category : Database.categories) {

            if (category.getStatus().equals(CategoryStatus.CREATE) && !category.getIsDelete()) {

                List<InlineKeyboardButton> row = new ArrayList<>();
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(language.equals(Language.UZ) ? category.getNameUz() : category.getNameRu());
                button.setCallbackData(":UPDATE" + bookType + category.getNameUz());
                row.add(button);
                rowList.add(row);

            }
        }

        List<InlineKeyboardButton> row4 = new ArrayList<>();
        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button4.setText(language.equals(Language.UZ) ? "\uD83D\uDD01 ORQAGA" :
                "\uD83D\uDD01 НАЗАД");
        button4.setCallbackData(DemoUtil.UPDATE_BOOK);
        row4.add(button4);
        rowList.add(row4);

        markup.setKeyboard(rowList);
        return markup;
    }

    public static InlineKeyboardMarkup getBookForUpdate(Language language, Category category, BookType bookType) {

        List<Book> bookList = Database.books.stream()
                .filter(book -> book.getStatusBook().equals(StatusBook.CREATE))
                .filter(book -> book.getCategory().getNameUz().equals(category.getNameUz())
                        && book.getBookType().name().equals(bookType.name())).toList();

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> row  = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();

        button.setText(language.equals(Language.UZ) ? "\uD83D\uDD01 ORQAGA" :
                "\uD83D\uDD01 НАЗАД");
        button.setCallbackData(bookType.equals(BookType.Audio_Book) ? DemoUtil.UPDATE_AUDIO :
                bookType.equals(BookType.Electron_Book) ? DemoUtil.UPDATE_EBOOK : DemoUtil.UPDATE_PBOOK);
        row.add(button);

        for (Book book : bookList) {
            List<InlineKeyboardButton> row1 = new ArrayList<>();
            InlineKeyboardButton button1 = new InlineKeyboardButton();
            button1.setText(book.getName());
            button1.setCallbackData("/update"+book.getName());
            row1.add(button1);
            rowList.add(row1);
        }
        rowList.add(row);
        markup.setKeyboard(rowList);

        return markup;
    }

    public static InlineKeyboardMarkup getBookUpdateField(Book book1, Language language) {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(language.equals(Language.UZ) ? "\uD83C\uDD95 NOMI VA MUALLIFI " :
                "\uD83C\uDD95 ИМЯ И АВТОР");
        button.setCallbackData(DemoUtil.UPDATE_NEW_NAME_AUTHOR + book1.getName());
        row.add(button);
        rowList.add(row);

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText(language.equals(Language.UZ) ? "\uD83D\uDCB8 YANGI NARX " :
                "\uD83D\uDCB8 НОВАЯ ЦЕНА");
        button1.setCallbackData(DemoUtil.UPDATE_NEW_PRICE + book1.getName());
        row1.add(button1);
        rowList.add(row1);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText(language.equals(Language.UZ) ? "\uD83C\uDF06 YANGI RASM " :
                "\uD83C\uDF06 НОВЫЙ РИСУНОК");
        button2.setCallbackData(DemoUtil.UPDATE_NEW_PHOTO + book1.getName());
        row2.add(button2);
        rowList.add(row2);

        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText(language.equals(Language.UZ) ? book1.getIsBlock() ? "⚠️ BLOCKDAN YECHISH"
                :"⚠️ BLOCKLAB QO'YISH" : book1.getIsBlock() ? "⚠️ РЕШЕНИЕ БЛОКА" :
                "⚠️БЛОКИРОВКА");
        button3.setCallbackData(DemoUtil.UPDATE_NEW_BLOCK + book1.getName());
        row3.add(button3);
        rowList.add(row3);

        List<InlineKeyboardButton> row4 = new ArrayList<>();
        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button4.setText(language.equals(Language.UZ) ? "\uD83D\uDD01 ORQAGA" :
                "\uD83D\uDD01 НАЗАД");
        button4.setCallbackData(":UPDATE" + book1.getBookType() + book1.getCategory().getNameUz());
        row4.add(button4);
        rowList.add(row4);

        markup.setKeyboard(rowList);

        return markup;
    }
}
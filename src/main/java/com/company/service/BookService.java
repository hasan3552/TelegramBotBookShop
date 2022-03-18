package com.company.service;

import com.company.Main;
import com.company.database.Database;
import com.company.enums.Language;
import com.company.enums.StatusBook;
import com.company.model.Book;
import com.company.model.Detail;
import com.company.model.User;
import com.company.util.FileJson;
import com.company.util.InlineKeyboardUtil;
import org.apache.poi.xssf.usermodel.*;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

public class BookService extends Thread {

    private Message message;
    private Language language;

    public BookService(Message message, Language language) {
        this.message = message;
        this.language = language;
    }


    public static void buyBook(User user, Book book, int countInt) {

        Optional<Detail> optional = user.getDetails().stream()
                .filter(detail -> detail.getBook().getId().equals(book.getId())).findAny();

        if (optional.isPresent()) {
            Detail detail = optional.get();
            detail.setNumber(detail.getNumber() + countInt);

        } else {
            Detail detail = new Detail(book, countInt);
            user.addDetail(detail);

        }
        FileJson.writeToUserJson(Database.users);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(user.getId());
        sendMessage.setText(user.getLanguage().equals(Language.UZ) ?
                "Kitob savatchangizda saqlandi." :
                "Kнига хранится в вашей корзине.");
        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

    }

    public static void updateNameAuthor(Book book, String text) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(Database.admin.getId());
        try {

            String[] split = text.split("/");
            book.setName(split[1]);
            book.setAuthor(split[2]);
            book.setStatusBook(StatusBook.CREATE);
            FileJson.writeToBookJson(Database.books);
            sendMessage.setText(Database.admin.getLanguage().equals(Language.UZ) ?
                    "Kitobning nomi va muallifi muvoffaqiyatli o'zgartirildi." :
                    "Название и автор книги успешно изменены.");
        } catch (Exception e) {
            sendMessage.setText(Database.admin.getLanguage().equals(Language.UZ) ?
                    "Kitobni nomi yoki authorni o'zgartirishda xatolik yuz berdi. Iltimos " +
                            "qaytadan urinib ko'ring." : "Произошла ошибка при переименовании " +
                    "книги или автора. Пожалуйста, попробуйте еще раз.");
        }

        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
    }

    public static void updatePrice(Book book, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(Database.admin.getId());

        try {
            book.setPrice(Double.valueOf(text));
            book.setStatusBook(StatusBook.CREATE);
            FileJson.writeToBookJson(Database.books);
            sendMessage.setText(Database.admin.getLanguage().equals(Language.UZ) ?
                    "Kitobning narxi o'zgartirildi." : "Цена книги изменилась.");

        } catch (Exception e) {

            sendMessage.setText(Database.admin.getLanguage().equals(Language.UZ) ?
                    "Kitob narxi o'zgartirishda xatolik yuz berdi." :
                    "Не удалось изменить цену книги.");
        }

        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

    }

    public void bookCRUD(Message message) {
        Language language = Database.admin.getLanguage();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setText(language.equals(Language.UZ) ? "Kitob service menyu." : "Заказать сервисное меню.");
        InlineKeyboardMarkup bookCrudForInline = InlineKeyboardUtil.getBookCrudForInline(language);
        sendMessage.setReplyMarkup(bookCrudForInline);

        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
    }

    public void hasPhoto(String fileId) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(Database.admin.getId());

        Book book = null;
        for (Book book1 : Database.books) {
            if (book1.getStatusBook().equals(StatusBook.NEW)) {
                book = book1;
                break;
            }
        }

        if (book != null) {

            book.setStatusBook(StatusBook.HAVE_PHOTO);
            book.setUrlPhoto(fileId);
            FileJson.writeToBookJson(Database.books);
            sendMessage.setText(Database.admin.getLanguage().equals(Language.UZ) ?
                    "Iltimos kitobning tipini tanlang:" : "Выберите тип книги:");
            InlineKeyboardMarkup bookType = InlineKeyboardUtil.getBookType(Database.admin.getLanguage());
            sendMessage.setReplyMarkup(bookType);

        } else {

            Optional<Book> optional = Database.books.stream()
                    .filter(book1 -> book1.getStatusBook().equals(StatusBook.UPDATE))
                    .findFirst();

            if (optional.isPresent()) {
                optional.get().setUrlPhoto(fileId);
                optional.get().setStatusBook(StatusBook.CREATE);
                FileJson.writeToBookJson(Database.books);
                sendMessage.setText(language.equals(Language.UZ) ? "Kitobga yangi rasm " +
                        "o'rnatildi." : "В книгу добавлена новая картинка.");
            } else{
                //sendMessage.setText("Tog'o oldin kitobni quwiwga kiring bot buziwdan oldin");
            }
        }

        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
    }

    @Override
    public void run() {

        XSSFWorkbook workbook = new XSSFWorkbook();

        try (FileOutputStream out = new FileOutputStream(Main.fileAllBook)) {

            XSSFSheet sheet = workbook.createSheet("ALL_BOOKS");

            XSSFRow row = sheet.createRow(0);
            row.createCell(0).setCellValue("ID");
            row.createCell(1).setCellValue("NAME");
            row.createCell(2).setCellValue("AUTHOR");
            row.createCell(3).setCellValue("CATEGORY");
            row.createCell(4).setCellValue("BOOK TYPE");
            row.createCell(5).setCellValue("IS_BLOCK");
            row.createCell(6).setCellValue("LANGUAGE");

            int i = 1;
            for (Book book : Database.books) {

                if (book.getStatusBook().equals(StatusBook.CREATE)) {

                    XSSFRow row1 = sheet.createRow(i++);
                    row1.createCell(0).setCellValue(book.getId());
                    row1.createCell(1).setCellValue(book.getName());
                    row1.createCell(2).setCellValue(book.getAuthor());
                    row1.createCell(3).setCellValue(language.equals(Language.UZ) ?
                            book.getCategory().getNameUz() : book.getCategory().getNameRu());
                    row1.createCell(4).setCellValue(book.getBookType().name());
                    row1.createCell(5).setCellValue(book.getIsBlock());
                    row1.createCell(6).setCellValue(book.getLanguage());
                }

                for (int j = 0; j < 8; j++) {
                    sheet.autoSizeColumn(j);
                }
            }

            workbook.write(out);
            out.close();

            SendDocument sendDocument = new SendDocument();
            InputFile inputFile = new InputFile(Main.fileAllBook);
            sendDocument.setDocument(inputFile);
            sendDocument.setChatId(String.valueOf(message.getChatId()));
            Main.MY_TELEGRAM_BOT.sendMsg(sendDocument);

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public SendMessage update() {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(Database.admin.getId());
        sendMessage.setText(language.equals(Language.UZ) ? "O'zgartilishi kerak bo'lgan kitob " +
                "tipini tanlang: " : "Выберите тип книги, которую хотите изменить:");

        InlineKeyboardMarkup forUpdate = InlineKeyboardUtil.bookTypeForUpdate(language);
        sendMessage.setReplyMarkup(forUpdate);

        return sendMessage;
    }
}
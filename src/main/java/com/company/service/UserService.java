package com.company.service;

import com.company.Main;
import com.company.database.Database;
import com.company.enums.BookType;
import com.company.enums.Language;
import com.company.model.*;
import com.company.util.DemoUtil;
import com.company.util.FileJson;
import com.company.util.InlineKeyboardUtil;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserService extends Thread {

    private User user;
    private String idStr;

    public UserService(User user, String idStr) {
        this.user = user;
        this.idStr = idStr;
    }

    public void buyBook(Message message) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(currentLanguage(message).equals(Language.UZ) ?
                "KITOB SOTIB OLISH UCHUN UNING TURINI TANLANG" : "ВЫБЕРИТЕ ТИП КНИГИ, ЧТОБЫ КУПИТЬ");
        sendMessage.setChatId(String.valueOf(message.getChatId()));

        InlineKeyboardMarkup bookType = InlineKeyboardUtil.getBookType(currentLanguage(message));
        sendMessage.setReplyMarkup(bookType);

        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
    }


    public void basket(User user, String id) {

        Language language = user.getLanguage();
        Double summa = 0.0;
        String products = (user.getLanguage().equals(Language.UZ) ? "Sizning savatchangizda: " :
                "В вашей корзине:") + "\n\n";
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(id);

        for (Detail detail : user.getDetails()) {

            products += detail.getBook().getName() + "\n" + detail.getBook().getPrice() +
                    " * " + detail.getNumber() + " = " + detail.getBook().getPrice() * detail.getNumber() +
                    (language.equals(Language.UZ) ? " so'm \nMuddati:" : " сум \nПродолжительность:") +
                    detail.getBuyTime() + "\n\n";
            summa += detail.getBook().getPrice() * detail.getNumber();

        }

        products += "\n\n" + (language.equals(Language.UZ) ? "JAMI : " : "ВСЕГО : ") + summa;
        InlineKeyboardMarkup basketForInline = InlineKeyboardUtil.getBasketForInline(language);

        if (user.getDetails().isEmpty() || user.getDetails() == null) {
            products = (language.equals(Language.UZ) ? "Savatchangiz hali bo'sh" :
                    "Ваша корзина все еще пуста");
        } else {
            sendMessage.setReplyMarkup(basketForInline);
        }

        sendMessage.setText(products);

        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
    }

    public void setting(Message message) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setReplyMarkup(InlineKeyboardUtil.getChangeLanguage());
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setText(currentLanguage(message).equals(Language.UZ) ?
                DemoUtil.WONT_SETTING_LAN_UZ : DemoUtil.WONT_SETTING_LAN_RU);

        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
    }

    public void setting(Language language) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setReplyMarkup(InlineKeyboardUtil.getChangeLanguage());
        sendMessage.setChatId(String.valueOf(Database.admin.getId()));
        sendMessage.setText(language.equals(Language.UZ) ?
                DemoUtil.WONT_SETTING_LAN_UZ : DemoUtil.WONT_SETTING_LAN_RU);

        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
    }

    public static Language currentLanguage(Message message) {

        Optional<com.company.model.User> optional = Database.users.stream()
                .filter(user -> String.valueOf(message.getChatId()).equals(user.getId()))
                .findFirst();

        Language language = optional.get().getLanguage();

        return language;
    }

    public void workCallbackQuery(CallbackQuery callbackQuery, User user) {

        String idStr = String.valueOf(callbackQuery.getFrom().getId());
        String data = callbackQuery.getData();

        if (data.contains("AUDIO")) {

            String abook = data.replace("AUDIO", "");
            Optional<Book> optional = Database.books.stream()
                    .filter(book -> book.getBookType().equals(BookType.Audio_Book))
                    .filter(audioBook -> audioBook.getName().equals(abook)).findAny();

            if (optional.isPresent()) {
                Book audioBook1 = optional.get();

                SendMessage sendMessage = new SendMessage();

                if (user.getDetails().stream()
                        .anyMatch(detail -> detail.getBook().getId().equals(audioBook1.getId()))) {

                    sendMessage.setText(user.getLanguage().equals(Language.UZ) ?
                            "Savatchada allaqachon ushbu elektron kitob saqlangan." :
                            "Эта электронная книга уже хранится в корзине.");

                } else {
                    Detail detail = new Detail(audioBook1, 1);
                    user.addDetail(detail);
                    FileJson.writeToUserJson(Database.users);
                    sendMessage.setText(user.getLanguage().equals(Language.UZ) ?
                            "Audio kitob savatchangizda saqlandi." :
                            "Аудиокнига хранится в вашей корзине.");

                    SendPhoto sendPhoto = new SendPhoto();
                    sendPhoto.setChatId(idStr);
                    InputFile inputFile = new InputFile(audioBook1.getUrlPhoto());
                    sendPhoto.setPhoto(inputFile);
                    Main.MY_TELEGRAM_BOT.sendMsg(sendPhoto);

                }

                sendMessage.setChatId(idStr);
                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
            }
        } else if (data.contains("EBOOK")) {

            String ebook = data.replace("EBOOK", "");

            Optional<Book> optional = Database.books.stream()
                    .filter(book -> book.getBookType().equals(BookType.Electron_Book))
                    .filter(eBook -> eBook.getName().equals(ebook))
                    .findAny();

            if (optional.isPresent()) {
                Book eBook1 = optional.get();
                SendMessage sendMessage = new SendMessage();

                if (user.getDetails().stream()
                        .anyMatch(detail -> detail.getBook().getId().equals(eBook1.getId()))) {

                    sendMessage.setText(user.getLanguage().equals(Language.UZ) ?
                            "Savatchada allaqachon ushbu elektron kitob saqlangan." :
                            "Эта электронная книга уже хранится в корзине.");

                } else {
                    Detail detail = new Detail(eBook1, 1);
                    user.addDetail(detail);
                    FileJson.writeToUserJson(Database.users);

                    SendPhoto sendPhoto = new SendPhoto();
                    sendPhoto.setChatId(idStr);
                    InputFile inputFile = new InputFile(eBook1.getUrlPhoto());
                    sendPhoto.setPhoto(inputFile);
                    Main.MY_TELEGRAM_BOT.sendMsg(sendPhoto);

                    sendMessage.setText(user.getLanguage().equals(Language.UZ) ?
                            "Elektron kitob savatchangizda saqlandi." :
                            "Электронная книга хранится в вашей корзине.");

                }

                sendMessage.setChatId(idStr);
                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
            }
        } else if (data.contains("PBOOK")) {

            String pbook = data.replace("PBOOK", "");
            Book printedBook1 = null;
            for (Book printedBook : Database.books) {
                if (printedBook.getName().equals(pbook) &&
                        printedBook.getBookType().equals(BookType.Printer_Book)) {
                    printedBook1 = printedBook;
                    break;
                }
            }
            data = "/pb/" + pbook + "/";
            InlineKeyboardMarkup countBook = InlineKeyboardUtil.getCountBook(data, user.getLanguage());
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(idStr);

            InputFile inputFile = new InputFile(printedBook1.getUrlPhoto());
            sendPhoto.setPhoto(inputFile);
            sendPhoto.setReplyMarkup(countBook);
            Main.MY_TELEGRAM_BOT.sendMsg(sendPhoto);

        } else if (data. startsWith("/pb/")) {

            String bookNameAndCount = data.replace("/pb/", "");
            String bookName = bookNameAndCount.substring(0, bookNameAndCount.indexOf('/'));
            String count = bookNameAndCount.substring(bookNameAndCount.indexOf('/') + 1);
            int countInt = Integer.parseInt(count);

            Optional<Book> optional = Database.books.stream()
                    .filter(book -> book.getBookType().equals(BookType.Printer_Book))
                    .filter(book -> book.getName().equals(bookName)).findAny();

            if (optional.isPresent()) {
                Book printedBook = optional.get();

                if (countInt == 0) {
                    Category category = printedBook.getCategory();
                    InlineKeyboardMarkup printBookForInline =
                            InlineKeyboardUtil.getBookForInline(user.getLanguage(), category,
                                    BookType.Printer_Book);

                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setText(user.getLanguage().equals(Language.UZ) ?
                            "Kitob toifasini tanlang" : "Выберите категорию книги");
                    sendMessage.setChatId(user.getId());
                    sendMessage.setReplyMarkup(printBookForInline);
                    Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

                } else {
                    BookService.buyBook(user, printedBook, countInt);
                }
            }
        }
    }

    @Override
    public void run() {
        Language language = user.getLanguage();

        if (!user.getHistorySale().isEmpty()) {

            String path = "src/main/resources/pdf_files/";
            File file = new File(path + idStr + ".pdf");

            List<SaleHistory> historySale = user.getHistorySale();

            try (PdfWriter writer = new PdfWriter(file)) {

                PdfDocument pdfDocument = new PdfDocument(writer);
                PdfPage pdfPage = pdfDocument.addNewPage();

                Document document = new Document(pdfDocument);

                Paragraph paragraph = new Paragraph(
                        (language.equals(Language.UZ)) ? "               ASSALOMU ALUYKUM " :
                                "             АССАЛОМУ АЛУЙКУМ");
                document.add(paragraph);

                paragraph = new Paragraph("\n");
                document.add(paragraph);

                Paragraph paragraph1 = new Paragraph(language.equals(Language.UZ) ?
                        "Sizning bizning botdan xarid qilgan kitoblaringiz" :
                        "Книги, которые вы купили у нашего бота");
                document.add(paragraph1);

                float[] pointColumnWidths = {3, 20, 14, 14, 5, 14, 14,16};
                Table table = new Table(pointColumnWidths);
                Table cell = table.addCell("T.R");
                Table cell1 = table.addCell("BOOK'S NAME  ");
                Table cell2 = table.addCell("BOOK'S STATUS");
                Table cell3 = table.addCell("CATEGORY   ");
                Table cell4 = table.addCell("NUMBER  ");
                Table cell5 = table.addCell("PRICE    ");
                Table cell6 = table.addCell("SUMMA    ");
                Table cell7 = table.addCell("DATE     ");
                document.add(table);

                int i = 1;
                double sum1 = 0.0;
                for (SaleHistory saleHistory : user.getHistorySale()) {

                    sum1 += saleHistory.getDetail().getNumber() * saleHistory.getDetail().getBook().getPrice();


                    Table table1 = new Table(pointColumnWidths);

                    cell = table1.addCell(String.valueOf(i++));
                    cell1 = table1.addCell(String.valueOf(saleHistory.getDetail().getBook().getName()));
                    cell2 = table1.addCell(user.getLanguage().equals(Language.UZ) ?
                            saleHistory.getStatusOrder().getNameUz():
                            saleHistory.getStatusOrder().getNameRu());
                    cell3 = table1.addCell(
                            language.equals(Language.UZ) ?
                                    saleHistory.getDetail().getBook().getCategory().getNameUz()
                                    : saleHistory.getDetail().getBook().getCategory().getNameRu());
                    cell4 = table1.addCell(String.valueOf(saleHistory.getDetail().getBuyTime()));
                    cell5 =
                            table1.addCell(String.valueOf(saleHistory.getDetail().getBook().getPrice()));
                    cell6 =
                            table1.addCell(String.valueOf(saleHistory.getDetail().getBook().getPrice()
                                    * saleHistory.getDetail().getNumber()));
                    cell7 =
                            table1.addCell(String.valueOf(saleHistory.getDetail().getBuyTime()));
                    table1.addCell(saleHistory.getDetail().getBuyTime());

                    document.add(table1);

                }
                Paragraph paragraph2 = new Paragraph("\n\n"+(language.equals(Language.UZ) ?
                        "Umumiy xaridlaringiz miqdori: " : "Общая сумма ваших покупок: ")+sum1);
                document.add(paragraph2);


                document.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            SendDocument sendDocument = new SendDocument();
            sendDocument.setChatId(idStr);
            InputFile inputFile = new InputFile(file);
            sendDocument.setDocument(inputFile);
            Main.MY_TELEGRAM_BOT.sendMsg(sendDocument);

        } else {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(idStr);
            sendMessage.setText(language.equals(Language.UZ) ? "Sizning xaridlar tarixingiz " +
                    "bo'sh" : "Ваша история покупок пуста");
            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        }
    }
}
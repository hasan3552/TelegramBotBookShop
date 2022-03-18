package com.company.service;

import com.company.Main;
import com.company.database.Database;
import com.company.enums.BookType;
import com.company.enums.CategoryStatus;
import com.company.enums.Language;
import com.company.enums.StatusBook;
import com.company.model.Book;
import com.company.model.Category;
import com.company.model.SaleHistory;
import com.company.model.User;
import com.company.util.DemoUtil;
import com.company.util.FileJson;
import com.company.util.InlineKeyboardUtil;
import com.company.util.KeyboardMarkupUtil;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class AdminService extends Thread {

    private Message message;

    public AdminService(Message message) {
        this.message = message;
    }

    public static void start(Message message) {

        SendMessage sendMessage = new SendMessage();
        Language language = Database.admin.getLanguage();
        sendMessage.setReplyMarkup(KeyboardMarkupUtil.getAdminMenu(language));

        sendMessage.setText("<b>ASSALOMU ALEYKUM</b>");
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setParseMode(ParseMode.HTML);

        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

    }

    @Override
    public void run() {

        List<User> users = Database.users.stream().sorted(getUserComparator()).toList();
        Language language = Database.admin.getLanguage();

        try (PdfWriter writer = new PdfWriter("src/main/resources/users.pdf")) {

            PdfDocument pdfDocument = new PdfDocument(writer);

            PdfPage pdfPage = pdfDocument.addNewPage();

            Document document = new Document(pdfDocument);

            Paragraph paragraph = new Paragraph();
            paragraph.add((language.equals(Language.UZ) ? "" +
                    "Botdan foydalanayotgan mijozlar ro'yxati." : "Список клиентов, использующих" +
                    " бота.").toUpperCase());

            Paragraph paragraph1 = new Paragraph(
                    (language.equals(Language.UZ)) ? "               ASSALOMU ALUYKUM " :
                            "             АССАЛОМУ АЛУЙКУМ");
            document.add(paragraph1);
            document.add(paragraph);

            paragraph = new Paragraph("\n");
            document.add(paragraph);

            float[] pointColumnWidths = {50, 300, 500, 300, 300};
            Table table = new Table(pointColumnWidths);
            Table cell = table.addCell("T/R");
            Table cell1 = table.addCell("CUSTOMER'S ID");
            Table cell2 = table.addCell("CUSTOMER'S NAME");
            Table cell5 = table.addCell("CONTACT");
            Table cell4 = table.addCell("GATE-MONEY");
            document.add(table);

            int i = 1;
            for (User user : users) {
                Double sum = 0.0;

                Table table1 = new Table(pointColumnWidths);

                for (SaleHistory saleHistory : user.getHistorySale()) {
                    sum += saleHistory.getDetail().getBook().getPrice();
                }
                cell = table1.addCell(String.valueOf(i++));
                cell1 = table1.addCell(user.getId());
                cell2 = table1.addCell(user.getFirstName());
                cell5 = table1.addCell(String.valueOf(user.getContact()));
                cell4 = table1.addCell(String.valueOf(sum));

                sum = 0.0;
                document.add(table1);

            }
            document.close();
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SendDocument sendDocument = new SendDocument();
        File file = new File("src/main/resources/users.pdf");
        InputFile inputFile = new InputFile(file);
        sendDocument.setDocument(inputFile);
        sendDocument.setChatId(String.valueOf(Database.admin.getId()));
        //sendDocument.se
        Main.MY_TELEGRAM_BOT.sendMsg(sendDocument);
    }

    private static Comparator<User> getUserComparator() {

        Comparator<User> comparator = new Comparator<User>() {
            Double sum = 0.0;

            @Override
            public int compare(User o1, User o2) {
                return o2.getHistorySale().size() - o1.getHistorySale().size();

            }
        };
        return comparator;
    }

    public static void orders(Message message) {

        List<User> users = Database.users;

        String path = "src/main/resources/orders.xlsx";
        File file = new File(path);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {

            XSSFWorkbook workbook = new XSSFWorkbook();

            XSSFSheet sheet = workbook.createSheet("Orders");

            XSSFRow row = sheet.createRow(0);
            row.createCell(0).setCellValue("T/R");
            row.createCell(1).setCellValue("CUSTOMER ID");
            row.createCell(2).setCellValue("CUSTOMER NAME");
            row.createCell(3).setCellValue("CONTACT");
            row.createCell(4).setCellValue("BOOK NAME");
            row.createCell(5).setCellValue("NUMBER");
            row.createCell(6).setCellValue("PRICE");
            row.createCell(7).setCellValue("SUMMA");
            row.createCell(8).setCellValue("ORDER STATUS");
            row.createCell(9).setCellValue("DATA");
            row.createCell(10).setCellValue("LOCATION");

            int i = 0;
            for (User user : users) {
                if (!user.getHistorySale().isEmpty()) {

                    for (SaleHistory saleHistory : user.getHistorySale()) {

                        if (!saleHistory.getDetail().getDelivered()) {

                            XSSFRow row2 = sheet.createRow(i + 1);

                            row2.createCell(0).setCellValue(i + 1);
                            row2.createCell(1).setCellValue(user.getId());
                            row2.createCell(2).setCellValue(user.getFirstName());
                            row2.createCell(3).setCellValue(user.getContact());
                            row2.createCell(4).setCellValue(saleHistory.getDetail().getBook().getName());
                            row2.createCell(5).setCellValue(saleHistory.getDetail().getNumber());
                            row2.createCell(6).setCellValue(saleHistory.getDetail().getBook().getPrice());
                            row2.createCell(7).setCellValue(
                                    saleHistory.getDetail().getBook().getPrice() * saleHistory.getDetail().getNumber());
                            row2.createCell(8).setCellValue((user.getLanguage().equals(Language.UZ)) ?
                                    saleHistory.getStatusOrder().getNameUz() :
                                    saleHistory.getStatusOrder().getNameRu());
                            row2.createCell(9).setCellValue(saleHistory.getDetail().getBuyTime());
                            row2.createCell(10).setCellValue(saleHistory.getLocation());

                            i = i + 1;
                        }
                    }
                }
            }
            for (int j = 0; j < 10; j++) {
                sheet.autoSizeColumn(j);
            }

            workbook.write(outputStream);
            outputStream.close();

            SendDocument sendDocument = new SendDocument();
            sendDocument.setChatId(String.valueOf(message.getChatId()));
            InputFile inputFile = new InputFile(file);
            sendDocument.setDocument(inputFile);
            Main.MY_TELEGRAM_BOT.sendMsg(sendDocument);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void createBook(Book book, String str) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(Database.admin.getId());
        try {

            String[] split = str.split("/");
            book.setName(split[1].trim());
            book.setAuthor(split[2].trim());
            book.setLanguage(split[3].trim());
            book.setPrice(Double.valueOf(split[4].trim()));
            book.setStatusBook(StatusBook.CREATE);
            FileJson.writeToBookJson(Database.books);

            sendMessage.setText(Database.admin.getLanguage().equals(Language.UZ) ?
                    "Kitob bazaga saqlandi." : "Книга хранится в базе данных.");
        } catch (Exception e) {

            sendMessage.setText(Database.admin.getLanguage().equals(Language.UZ) ?
                    "Xatolik yuz berdi.Iltimos boshqatan urinib ko'ring." :
                    "Произошла ошибка. Пожалуйста, попробуйте еще раз.");
        }
        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

    }

    public void workCallbackQuery(CallbackQuery callbackQuery) {

        SendMessage sendMessage = new SendMessage();

        String data = callbackQuery.getData();
        Language language = Database.admin.getLanguage();
        String idStr = String.valueOf(Database.admin.getId());

        sendMessage.setChatId(idStr);

        if (data.equals("change_language")) {

            sendMessage.setText(language.equals(Language.UZ) ? "Язык успешно изменен." : "Til muvvoffaqiyatli " +
                    "o'zgartilirdi.");

            Database.admin.setLanguage(language.equals(Language.UZ) ? Language.RU : Language.UZ);
             FileJson.writeToAdminJson();

            sendMessage.setReplyMarkup(KeyboardMarkupUtil.getAdminMenu(Database.admin.getLanguage()));

        } else if (data.equals("don't_change_language")) {

            sendMessage.setText(!language.equals(Language.UZ) ? "Язык остался без изменений." :
                    "Til o'zgarishsiz qoldirildi.");

        } else if (data.equals(DemoUtil.CREATE_BOOK)) {

            sendMessage.setText(language.equals(Language.UZ) ? "Kitobning rasmini uzating." :
                    "Отправьте фото книги.");
            if (Database.books.stream()
                    .allMatch(book -> book.getStatusBook().equals(StatusBook.CREATE))) {


                Book book = new Book();
                book.setId(Database.books.size()+1);
                FileJson.writeToBookJson(book);
            } else {

                sendMessage.setText("Oldin bittasini eplang.");
            }
        } else if (data.equals(DemoUtil.CHANGE_ELECTRON_BOOKS) || data.equals(DemoUtil.CHANGE_PRINT_BOOKS)
                || data.equals(DemoUtil.CHANGE_AUDIO_BOOKS)) {

            BookType bookType = (data.equals(DemoUtil.CHANGE_ELECTRON_BOOKS) ?
                    BookType.Electron_Book : data.equals(DemoUtil.CHANGE_AUDIO_BOOKS) ?
                    BookType.Audio_Book : BookType.Printer_Book);
            Optional<Book> optional = Database.books.stream()
                    .filter(book -> book.getStatusBook().equals(StatusBook.HAVE_PHOTO))
                    .findAny();
            optional.get().setStatusBook(StatusBook.IN_PROGRESS1);
            optional.get().setBookType(bookType);
            FileJson.writeToBookJson(Database.books);
            sendMessage.setText(language.equals(Language.UZ) ? "Iltimos kitob kategoriyasini tanlang:"
                    : "Пожалуйста, выберите категорию книги:");
            InlineKeyboardMarkup categoryBooks = InlineKeyboardUtil.getCategoryBooks(language, data);
            sendMessage.setReplyMarkup(categoryBooks);

        } else if (data.equals(DemoUtil.CHANGE_AUDIO_BOOKS_DELETE) ||
                data.equals(DemoUtil.CHANGE_ELECTRON_BOOKS_DELETE) ||
                data.equals(DemoUtil.CHANGE_PRINT_BOOKS_DELETE)) {

            sendMessage.setText(language.equals(Language.UZ) ?
                    "O'chirilishi kerak bo'lgan kitob kategoriyasini tanlang." :
                    "Выберите категорию книг, которую необходимо удалить.");
            sendMessage.setReplyMarkup(InlineKeyboardUtil.getCategoryBooksForDelete(language, data));

        } else if (data.contains(DemoUtil.CHANGE_AUDIO_BOOKS_DELETE) ||
                data.contains(DemoUtil.CHANGE_ELECTRON_BOOKS_DELETE) ||
                data.contains(DemoUtil.CHANGE_PRINT_BOOKS_DELETE)) {

            sendMessage.setText(language.equals(Language.UZ) ? "O'chirmoqchi bo'lgan " +
                    "kitobingizni tanlang:" : "Выберите книгу, которую хотите удалить:");
            Category category1 = null;
            for (Category category : Database.categories) {
                if (data.contains(category.getNameUz())) {
                    category1 = category;
                    break;
                }
            }

            BookType bookType = (data.contains(DemoUtil.CHANGE_AUDIO_BOOKS_DELETE) ?
                    BookType.Audio_Book : data.contains(DemoUtil.CHANGE_ELECTRON_BOOKS_DELETE) ?
                    BookType.Electron_Book : BookType.Printer_Book);

            InlineKeyboardMarkup bookForInlineDelete =
                    InlineKeyboardUtil.getBookForInlineDelete(language, category1, bookType);

            sendMessage.setReplyMarkup(bookForInlineDelete);

        } else if (data.contains("DELETEPB") || data.contains("DELETEEB") || data.contains(
                "DELETEAB")) {

            BookType bookType = (data.contains("DELETEAB") ? BookType.Audio_Book :
                    data.contains("DELETEEB") ? BookType.Electron_Book : BookType.Printer_Book);


            String bookName = data.replace("DELETEPB", "").replace("DELETEEB", "")
                    .replace("DELETEAB", "");

            Optional<Book> optional = Database.books.stream()
                    .filter(book -> book.getBookType().equals(bookType))
                    .filter(book -> book.getName().equals(bookName)).findFirst();

            optional.get().setIsBlock(true);

            FileJson.writeToBookJson(Database.books);

            sendMessage.setText(language.equals(Language.UZ) ?
                    "Kitob bazadan o'chirildi." : "Книга была удалена из базы данных.");


        } else if (data.contains(DemoUtil.CHANGE_AUDIO_BOOKS) ||
                data.contains(DemoUtil.CHANGE_PRINT_BOOKS) || data.contains(DemoUtil.CHANGE_ELECTRON_BOOKS)) {

            Category category1 = null;
            for (Category category : Database.categories) {
                if (data.contains(category.getNameUz())) {
                    category1 = category;
                    break;
                }
            }
            Optional<Book> optional = Database.books.stream()
                    .filter(book -> book.getStatusBook().equals(StatusBook.IN_PROGRESS1))
                    .findAny();
            optional.get().setCategory(category1);
            optional.get().setStatusBook(StatusBook.IN_PROGRESS2);
            FileJson.writeToBookJson(Database.books);

            sendMessage.setText(language.equals(Language.UZ) ? "Iltimos ushbu farmatda " +
                    "kiriting\n\n" + "/nomi/muallifi/qaysi tildaligi/narxi/" :
                    "Пожалуйста, введите этот формат\n\n" +
                            "/имя/автор/на каком языке книга/цена/");
        } else if (data.equals(DemoUtil.SHOW_ALL_BOOKS)) {

            sendMessage.setText(language.equals(Language.UZ) ? "Barcha kitoblar ro'yhati." : "Список всех книг.");
            BookService bookService = new BookService(message,language);
            bookService.start();

        } else if (data.equals(DemoUtil.DELETE_BOOK)) {

            sendMessage.setText(language.equals(Language.UZ) ?
                    "O'chirmoqchi bo'lgan kitob tipining tanlang." :
                    "Выберите тип книги, которую хотите удалить.");
            sendMessage.setReplyMarkup(InlineKeyboardUtil.getBookTypeForDelete(language));

        } else if (data.equals("_orqaga_delete")) {

            sendMessage.setText(language.equals(Language.UZ) ? "Kitob service menyu." : "Заказать сервисное меню.");
            sendMessage.setReplyMarkup(InlineKeyboardUtil.getBookCrudForInline(language));

        } else if (data.equals(DemoUtil.UPDATE_BOOK)) {

            BookService bookService = new BookService(message,language);
            sendMessage = bookService.update();

        }else if (data.equals(DemoUtil.UPDATE_AUDIO) || data.equals(DemoUtil.UPDATE_EBOOK) ||
        data.equals(DemoUtil.UPDATE_PBOOK)){

            BookType bookType = data.equals(DemoUtil.UPDATE_PBOOK) ? BookType.Printer_Book :
                    data.equals(DemoUtil.UPDATE_EBOOK) ? BookType.Electron_Book :
                            BookType.Audio_Book;

            InlineKeyboardMarkup categoryForUpdate =
                    InlineKeyboardUtil.getBookCategoryForUpdate(language, bookType);
            sendMessage.setText(language.equals(Language.UZ) ? "O'zgartirilishi kerak bo'lgan " +
                    "kitob kategoriyasini tanlang." : "Выберите категорию книги, которую нужно изменить.");
            sendMessage.setReplyMarkup(categoryForUpdate);

        }else if (data.startsWith(":UPDATE")){

            BookType bookType = null;
            bookType = data.contains(BookType.Electron_Book.name()) ? BookType.Electron_Book :
                    data.contains(BookType.Audio_Book.name()) ? BookType.Audio_Book :
                            BookType.Printer_Book;

            Optional<Category> optional = Database.categories.stream()
                    .filter(category -> category.getStatus().equals(CategoryStatus.CREATE) &&
                            !category.getIsDelete())
                    .filter(category -> data.contains(category.getNameUz())).findAny();

            InlineKeyboardMarkup bookForUpdate =
                    InlineKeyboardUtil.getBookForUpdate(language, optional.get(), bookType);
            sendMessage.setText(language.equals(Language.UZ) ? "O'zgartirilishi kerak bo'lgan " +
                    "kitobni tanlang." : "Выберите книгу, которую нужно изменить.");
            sendMessage.setReplyMarkup(bookForUpdate);


        }else if (data.startsWith("/update")){

            Optional<Book> book1 = Database.books.stream()
                    .filter(book -> data.contains(book.getName())).findAny();

//            book1.get().setStatusBook(StatusBook.UPDATE);
//            FileJson.writeToBookJson(Database.books);

            InlineKeyboardMarkup bookUpdateField =
                    InlineKeyboardUtil.getBookUpdateField(book1.get(), language);
            sendMessage.setReplyMarkup(bookUpdateField);
            sendMessage.setText(language.equals(Language.UZ) ? "O'zgarishni tanlang." : "Выберите Изменить.");

        }else if (data.startsWith(DemoUtil.UPDATE_NEW_PRICE) ||data.startsWith(DemoUtil.UPDATE_NEW_NAME_AUTHOR)
                || data.startsWith(DemoUtil.UPDATE_NEW_PHOTO)){

            Optional<Book> optional = Database.books.stream()
                    .filter(book -> data.contains(book.getName())).findAny();

            optional.get().setStatusBook(StatusBook.UPDATE);
            FileJson.writeToBookJson(Database.books);

            sendMessage.setText(data.startsWith(DemoUtil.UPDATE_NEW_PHOTO) ?
                    language.equals(Language.UZ) ? "Yangi rasmni jo'nating." : "Отправить новую " +
                            "картинку." : data.startsWith(DemoUtil.UPDATE_NEW_NAME_AUTHOR) ?
                    language.equals(Language.UZ) ? "Yangi nom va muallifni o'rnatish uchun " +
                            "iltimos ushbu farmatda yozing\n\n /nomi/muallifi/UPDATE" :"" +
                            "Чтобы установить новое имя и автора, введите в этом формате \n\n " +
                            "/имя/автор/UPDATE" : language.equals(Language.UZ) ? "Yangi narxni " +
                    "kiriting:" :"Введите новую цену.");
        }else if (data.startsWith(DemoUtil.UPDATE_NEW_BLOCK)){

            Optional<Book> optional = Database.books.stream()
                    .filter(book -> data.contains(book.getName())).findAny();
            sendMessage.setText(optional.get().getIsBlock() ? language.equals(Language.UZ) ?
                    "Kitob qayta faollashtirildi." : "Книга была возобновлена." :
                    language.equals(Language.UZ) ? "Kitob bloklab qo'yildi." :"Книга заблокирована.");

            optional.get().setIsBlock(!optional.get().getIsBlock());
            FileJson.writeToBookJson(Database.books);
        }
        else if (data.equals(DemoUtil.CHANGE_CATEGORY_SHOW)) {

            sendMessage = CategoryService.show(language);

        } else if (data.equals(DemoUtil.CHANGE_CATEGORY_CREATE)) {

            sendMessage.setChatId(idStr);
            if (Database.categories.stream()
                    .allMatch(category -> category.getStatus().equals(CategoryStatus.CREATE))) {

                sendMessage.setText(language.equals(Language.UZ) ? "" +
                        "Kategoriya qo'shish uchun iltimos ushbu formatta yozing:\n\n/nomi(o'zbek " +
                        "tilida)/nomi(rus tilida)/" : "Для добавления категории пишите в таком формате: " +
                        "\\n\\n/имя (на узбекском языке)/имя (на русском языке)/");
                Category category = new Category(Database.categories.size()+1);
                category.setStatus(CategoryStatus.NEW);
                Database.categories.add(category);
                FileJson.writeToCaregoryJson(Database.categories);
            }else {
                sendMessage.setText(language.equals(Language.UZ) ?
                        "Oldin bittasini categoriyani tuliq create qilip quying." : "" +
                        "Сначала залейте один, чтобы полностью создать категорию.");
            }
        }else if (data.equals(DemoUtil.CHANGE_CATEGORY_UPDATE)){
            String text = CategoryService.show(language).getText()+"\n\n";
            text += (language.equals(Language.UZ) ? "O'zgartirish uchun iltimos ushbu farmatda " +
                    "yozing.\n\nUPDATECATE/id/qo'yilishi kerak o'zbekcha nomi/qo'yilishi kerak " +
                    "ruscha nomi/"
                    : "Чтобы изменить, пожалуйста, напишите в этом формате.\n\nUPDATE/id/должно " +
                    "быть вставлено узбекское имя/должно быть вставлено русское имя/");
            sendMessage.setText(text);
        }else if (data.equals(DemoUtil.CHANGE_CATEGORY_DELETE)){
            String text = CategoryService.show(language).getText()+"\n\n";
            text +=(language.equals(Language.UZ) ? "O'chirish uchun iltimos ushbu farmatda yozing"
                    +"\n\nDELETECATE/id(o'chirilishi kerak bo'lgan)/" : "Для удаления введите в " +
                    "этом формате \n\nDELETECATE/id (для удаления)/");
            sendMessage.setText(text);
        }

        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setMessageId(callbackQuery.getMessage().getMessageId());
        deleteMessage.setChatId(idStr);
        Main.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
    }
}
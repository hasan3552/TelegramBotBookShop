package com.company.controller;

import com.company.Main;
import com.company.database.Database;
import com.company.enums.CategoryStatus;
import com.company.enums.StatusBook;
import com.company.model.Book;
import com.company.service.AdminService;
import com.company.service.BookService;
import com.company.service.CategoryService;
import com.company.service.UserService;
import com.company.util.DemoUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.List;
import java.util.Optional;

public class AdminController extends Thread {

    private Message message;


    public AdminController(Message message) {
        this.message = message;
    }

    @Override
    public void run() {

        if (message.hasText()) {

            if (message.getText().equals("/start")) {
                AdminService.start(message);
            } else if (message.getText().equals(DemoUtil.BOOK_CRUD_UZ) || message.getText().equals(DemoUtil.BOOK_CRUD_RU)) {
                BookService bookService = new BookService(message,Database.admin.getLanguage());
                bookService.bookCRUD(message);
            } else if (message.getText().equals(DemoUtil.SETTING_RU) || message.getText().equals(DemoUtil.SETTING_UZ)) {
                UserService userService = new UserService(Database.admin,Database.admin.getId());
                userService.setting(Database.admin.getLanguage());
            } else if (message.getText().equals(DemoUtil.USERS_RU) || message.getText().equals(DemoUtil.USERS_UZ)) {
                AdminService adminService = new AdminService(message);
                adminService.start();
            } else if (message.getText().equals(DemoUtil.ORDERS_UZ) || message.getText().equals(DemoUtil.ORDERS_RU)) {
                AdminService.orders(message);
            } else if (message.getText().equals(DemoUtil.CATEGORY_CRUD_UZ) ||
                    message.getText().equals(DemoUtil.CATEGORY_CRUD_RU)) {

                CategoryService.crud();
            } else {

                Optional<Book> optional = Database.books.stream()
                        .filter(book -> book.getStatusBook().equals(StatusBook.IN_PROGRESS2))
                        .findAny();

                Optional<Book> optional1 = Database.books.stream()
                        .filter(book -> book.getStatusBook().equals(StatusBook.UPDATE))
                        .findAny();

                if (optional.isPresent()) {
                    AdminService.createBook(optional.get(), message.getText());

                } else if ((message.getText().startsWith("UPDATECATE/"))) {
                    String text = message.getText().replace("UPDATECATE", "");
                    CategoryService.update(text);
                } else if (message.getText().startsWith("DELETECATE/")) {
                    String text = message.getText().replace("DELETECATE", "");
                    CategoryService.delete(text);
                } else if (Database.categories.stream()
                        .anyMatch(category -> category.getStatus().equals(CategoryStatus.NEW))) {

                    CategoryService.create(message.getText());

                }else if (optional1.isPresent()){

                    if (message.getText().endsWith("/UPDATE")){

                        BookService.updateNameAuthor(optional1.get(),message.getText());
                    }else {
                        BookService.updatePrice(optional1.get(),message.getText());
                    }
                }
                else {

                    SendMessage sendMessage = new SendMessage(String.valueOf(message.getChatId()),
                            message.getText());
                    Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
                }
            }
        } else if (message.hasPhoto()) {
            BookService bookService = new BookService(message,Database.admin.getLanguage());
            List<PhotoSize> photo = message.getPhoto();
            String fileId = photo.get(photo.size() - 1).getFileId();

            bookService.hasPhoto(fileId);
        }
    }

}
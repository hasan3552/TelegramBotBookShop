package com.company.database;

import com.company.model.*;
import org.telegram.telegrambots.meta.api.objects.Contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Database {

    public static User admin = new User("2108287093","Hasan","Fayzullayev","+998916453552",
            "admin");
    public static List<User> users = new ArrayList<>();
    public static List<Category> categories = new ArrayList<>();
    public static List<Book> books = new ArrayList<>();
    public static Map<Long,Integer> twilioCode = new HashMap<>();
    public static Map<Long, Contact> contactMap = new HashMap<>();

//
//    public static void compile (){
//
//        categories.add(Category.CHILDREN_LIBR);
//        categories.add(Category.UZBEK_LIBR);
//        categories.add(Category.WORLD_LIBR);
//
//        FileJson.writeToCaregoryJson(categories);
//
//        FileJson.writeToUserJson(Database.users);
//
////        Book book = new Book("Uzbek","Sariq devni minib",Category.CHILDREN_LIBR,
////                "Hudoyberdi To'xtaboyev",30000d,
////                "https://assets.asaxiy.uz/product/items/desktop/90.jpg",BookType.Printer_Book);
////
////        Book book1 = new Book("Uzbek","Shaytanat (1-qism)",Category.UZBEK_LIBR,"Tohir Malik",
////                56000d,"src/main/resources/photos/s3579.jpg", BookType.Printer_Book);
////
////        books.add(book);
////        books.add(book1);
//
//        FileJson.writeToBookJson(books);
//        //System.out.println("adadaadad");
//
//
//    }

}

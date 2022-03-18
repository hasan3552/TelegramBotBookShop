package com.company.util;

import com.company.Main;
import com.company.database.Database;
import com.company.model.Book;
import com.company.model.Category;
import com.company.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.telegram.telegrambots.meta.api.objects.Contact;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FileJson {
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void readFromUserJson() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DemoUtil.userJsonFile))) {

            Type type = new TypeToken<List<User>>() {
            }.getType();
            users = gson.fromJson(reader, type);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Database.users = users;

    }

    public static void writeToAdminJson() {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Main.fileAdmin))) {

            gson.toJson(Database.admin, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void readAdminJson() {

        try (BufferedReader reader = new BufferedReader(new FileReader(Main.fileAdmin))) {

            Database.admin = gson.fromJson(reader, User.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void readContactMap() {

        try (BufferedReader reader = new BufferedReader(new FileReader(Main.fileContact))) {

            Type type = new TypeToken<Map<Long, Contact>>() {
            }.getType();

            Database.contactMap = gson.fromJson(reader, type);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void writeContactMap(Map<Long, Contact> map) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Main.fileContact))) {

            Type type = new TypeToken<Map<Long, Contact>>() {
            }.getType();

            gson.toJson(map,writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeToUserJson(User user) {

        Database.users.add(user);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Main.file))) {

            gson.toJson(Database.users, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeToUserJson(List<User> users) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Main.file))) {

            gson.toJson(users, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readFromBooksJson() {
        List<Book> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(Main.fileBook))) {

            Type type = new TypeToken<List<Book>>() {
            }.getType();
            books = gson.fromJson(reader, type);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Database.books = books;
    }

    public static void writeToBookJson(Book book) {

        Database.books.add(book);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Main.fileBook))) {

            gson.toJson(Database.books, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeToBookJson(List<Book> books) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Main.fileBook))) {

            gson.toJson(books, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeToCaregoryJson(List<Category> categories) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Main.fileCategory))) {

            gson.toJson(categories, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readFromCategoryJson() {
        List<Category> categories = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(Main.fileCategory))) {

            Type type = new TypeToken<List<Category>>() {
            }.getType();
            categories = gson.fromJson(reader, type);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Database.categories = categories;
    }

    public static void writeTwilioCode(Map<Long, Integer> twilioCode) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Main.fileTwilio))) {

            gson.toJson(twilioCode,writer);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void readFromTwilioCode() {

        try (BufferedReader reader = new BufferedReader(new FileReader(Main.fileTwilio))) {

            Type type = new TypeToken<Map<Long, Integer>>() {
            }.getType();

            Database.twilioCode = gson.fromJson(reader, type);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

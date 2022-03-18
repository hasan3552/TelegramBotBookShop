package com.company.model;

import com.company.enums.BookType;
import com.company.enums.StatusBook;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Book {

    private Integer id = null;
    private String name = null;
    private String author = null;
    private BookType bookType = null;
    private Category category = null;
    private Boolean isBlock = false;
    private Double price = null;
    private String language = null;
    private String urlPhoto = null;
    private StatusBook statusBook = StatusBook.NEW;

    public Book() {
    }

    public Book(String language, String name, Category category, String author, Double price,
                String urlPhoto, BookType bookType, StatusBook statusBook) {

        this.statusBook = statusBook;
        this.bookType = bookType;
        this.language = language;
        this.author = author;
        this.name = name;
        this.category = category;
        isBlock = false;
        this.price = price;
        this.urlPhoto = urlPhoto;
    }

}

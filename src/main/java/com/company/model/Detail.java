package com.company.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
public class Detail {

    private String buyTime;
    private Book book;
    private Integer number = 0;
    private Boolean delivered = false;

    public String formatter(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy  HH:mm");
        String format = LocalDateTime.now().format(formatter);
        return format;
    }

    public Detail(Book book, Integer number) {
        this.number = number;
       buyTime = formatter();
       this.book = book;
    }

}

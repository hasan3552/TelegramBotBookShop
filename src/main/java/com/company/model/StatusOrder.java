package com.company.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StatusOrder {

    public static final StatusOrder NEW  = new StatusOrder(1,"YANGI","НОВЫЙ");
    public static final StatusOrder IN_PROGRESS = new StatusOrder(2,"JARAYONDA","В ХОДЕ " +
            "ВЫПОЛНЕНИЯ");
    public static final StatusOrder IN_WAY = new StatusOrder(3,"YETKAZILMOQDA","НАКАНУНЕ " +
            "ДОСТАВКИ");
    public static final StatusOrder DELIVERED = new StatusOrder(4,"YETKAZILDI","ДОСТАВЛЕН");

    private Integer id;
    private String nameUz;
    private String nameRu;

    StatusOrder(Integer id, String nameUz, String nameRu) {
        this.id = id;
        this.nameUz = nameUz;
        this.nameRu = nameRu;
    }
}

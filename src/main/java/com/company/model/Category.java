package com.company.model;

import com.company.enums.CategoryStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Category {

    public static final Category WORLD_LIBR= new Category("\uD83C\uDFF3️ JAHON ADABIYOTI",
            "\uD83C\uDFF3️ МИРОВАЯ ЛИТЕРАТУРА");
    public static final Category UZBEK_LIBR= new Category("\uD83C\uDDFA\uD83C\uDDFF UZBEK ADABIYOTI",
            "\uD83C\uDDFA\uD83C\uDDFF УЗБЕКСКАЯ ЛИТЕРАТУРА");
    public static final Category CHILDREN_LIBR= new Category("\uD83E\uDDD9\u200D♀ BOLALAR ADABIYOTI",
            "\uD83E\uDDD9\u200D♀ ДЕТСКАЯ ЛИТЕРАТУРА");

    private Integer id;
    private String nameUz;
    private String nameRu;
    private Boolean isDelete = false;
    private CategoryStatus status;


    public Category(Integer id) {
        this.id = id;
    }

    public Category(String nameUz, String nameRu) {

        this.nameUz = nameUz;
        this.nameRu = nameRu;
        isDelete = false;
    }
}

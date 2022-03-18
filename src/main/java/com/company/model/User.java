package com.company.model;

import com.company.enums.Language;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.telegram.telegrambots.meta.api.objects.Location;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class User {

    private String id;
    private String firstName;
    private String lastName;
    private String contact;
    private String username;
    private Language language = Language.UZ;
    private List<Detail> details;
    private List<SaleHistory> historySale = new ArrayList<>();
    private Boolean isBlock;

    public User(String id, String firstName, String lastName, String contact,String username) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contact = contact;
        this.username = username;
        isBlock = false;
        details = new ArrayList<>();

    }
    public void addDetail(Detail detail){
        details.add(detail);
    }

    public void addToHistorySale(List<Detail> detailList, Location location){
        List<SaleHistory> list = new ArrayList<>();

        for (Detail detail : detailList) {
            SaleHistory saleHistory =
                    new SaleHistory(("late:" + location.getLatitude()+" lng: "+ location.getLongitude()),
                    detail);
            list.add(saleHistory);
        }


        historySale.addAll(list);
        System.out.println("historySale.size() = " + historySale.size());
        // FileJson.writeToUserJson(Database.users);
    }

}

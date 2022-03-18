package com.company.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SaleHistory  {

    private Detail detail;
    private String location;
    private StatusOrder statusOrder;

    public SaleHistory(String location, Detail detail) {
        statusOrder = StatusOrder.NEW;
        this.location = location;
        this.detail = detail;
    }
}

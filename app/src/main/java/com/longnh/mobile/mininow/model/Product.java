package com.longnh.mobile.mininow.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Product implements Serializable {

    private long id;
    private Store store;
    private String name;
    private int price;
    private String imgUrl;
    private String description;

}

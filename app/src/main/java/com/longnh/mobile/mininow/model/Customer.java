package com.longnh.mobile.mininow.model;

import lombok.Data;

@Data
public class Customer {

    private long id;
    private String uid;
    private String address;
    private String name;
    private String phone;
    private String imgURL;
}

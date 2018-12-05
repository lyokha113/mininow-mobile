package com.longnh.mobile.mininow.model;


import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Order {

    private long id;
    private Store store;
    private Customer customer;
    private Shipper shipper;
    private long productPrice;
    private long shipPrice;
    private LocalDateTime orderTime;
    private LocalDateTime expectedTime;
    private LocalDateTime finishedTime;
    private String description;
    private int status;
}

package com.longnh.mobile.mininow.model;

import java.util.Set;

import lombok.Data;

@Data
public class ProductExtra {

    private long id;
    private Product product;
    private String name;
    private int value;
    private boolean required;
}

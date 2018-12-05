package com.longnh.mobile.mininow.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class OrderItem {

    private long id;
    private long productID;
    private int price;
    private int quantity;
    private Set<ProductExtra> extras;
    private int totalPrice;
    private String description;
    private String name;

    public OrderItem() {
        this.extras = new HashSet<>();
    }

    public int getTotalPrice() {
        int total = price;
        for (ProductExtra extra : extras) {
            total += extra.getValue();
        }
        return total * quantity;
    }

}

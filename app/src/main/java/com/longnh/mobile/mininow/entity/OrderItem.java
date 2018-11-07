package com.longnh.mobile.mininow.entity;

import java.sql.Timestamp;

public class OrderItem {

    private String productID;
    private int price;
    private int quantity;
    private int requireExtra;
    private int optionalExtra;
    private int totalPrice;
    private String description;
    private Timestamp time;
    private String name;

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getRequireExtra() {
        return requireExtra;
    }

    public void setRequireExtra(int requireExtra) {
        this.requireExtra = requireExtra;
    }

    public int getOptionalExtra() {
        return optionalExtra;
    }

    public void setOptionalExtra(int optionalExtra) {
        this.optionalExtra = optionalExtra;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTotalPrice() {
        return (price + optionalExtra + requireExtra) * quantity;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

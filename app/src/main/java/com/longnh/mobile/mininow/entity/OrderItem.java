package com.longnh.mobile.mininow.entity;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class OrderItem {

    private String productID;
    private int price;
    private int quantity;
    private Map<String, Integer> requireExtra;
    private Map<String, Integer> optionalExtra;
    private int totalPrice;
    private String description;
    private String name;

    public OrderItem() {
        this.requireExtra = new HashMap<>();
        this.optionalExtra = new HashMap<>();
    }

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

    public Map<String, Integer> getRequireExtra() {
        return requireExtra;
    }

    public void setRequireExtra(Map<String, Integer> requireExtra) {
        this.requireExtra = requireExtra;
    }

    public Map<String, Integer> getOptionalExtra() {
        return optionalExtra;
    }

    public void setOptionalExtra(Map<String, Integer> optionalExtra) {
        this.optionalExtra = optionalExtra;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTotalPrice() {
        int total = price;
        for (Map.Entry<String, Integer> item : requireExtra.entrySet()) {
            total += item.getValue();
        }
        for (Map.Entry<String, Integer> item : optionalExtra.entrySet()) {
            total += item.getValue();
        }
        return total * quantity;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
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

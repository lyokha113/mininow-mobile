package com.longnh.mobile.mininow.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Product implements Serializable {

    private String id;
    private String storeID;
    private String name;
    private int price;
    private String description;
    private String imgUrl;
    private ArrayList<String> requireExtra;
    private ArrayList<String> optionalExtra;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public ArrayList<String> getRequireExtra() {
        return requireExtra;
    }

    public void setRequireExtra(ArrayList<String> requireExtra) {
        this.requireExtra = requireExtra;
    }

    public ArrayList<String> getOptionalExtra() {
        return optionalExtra;
    }

    public void setOptionalExtra(ArrayList<String> optionalExtra) {
        this.optionalExtra = optionalExtra;
    }
}

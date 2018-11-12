package com.longnh.mobile.mininow.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Product implements Serializable {

    private String id;
    private String storeID;
    private String name;
    private int price;
    private String description;
    private String imgUrl;
    private Map<String, Integer> requireExtra;
    private Map<String, Integer> optionalExtra;

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
}

package com.longnh.mobile.mininow.entity;

public class OrderItem {

    private String storeID;
    private String productID;
    private int price;
    private int quantity;
    private int requireExtra;
    private int optionalExtra;

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
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

}

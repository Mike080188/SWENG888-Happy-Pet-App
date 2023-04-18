package com.happypet.model;

import java.io.Serializable;

public class Product implements Serializable {
    
//    private String title;
    private String name;
    private String seller;
    private String description;
    private Double price;
    private String storeAddress;
    private String storeName;
    private String imageUri;
    private String Key;


    public Product() {
    }


    public Product(String name, String seller, String description, Double price) {
        this.name = name;
        this.seller = seller;
        this.description = description;
        this.price = price;
    }

    /** Getters and Setters */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    public void setKey(String key) {
        Key = key;
    }

    public String getKey() {
        return Key;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getImageUri() {
        return imageUri;
    }
}

package com.happypet.model;

public class CartItem {

    private int quantity;
    private String name;
    private String description;
    private Double price;

    private String storeAddress;
    private String storeName;
    private String imageUri;

    private String Key;

    public CartItem(int quantity, String name, String description, Double price, String storeName, String storeAddress, String imageUri) {
        this.quantity = quantity;
        this.name = name;
        this.description = description;
        this.price = price;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.imageUri = imageUri;
    }

    public CartItem() {
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
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

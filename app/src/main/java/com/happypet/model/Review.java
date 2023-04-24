package com.happypet.model;

public class Review {
    private String userID;
    private String productKey;
    private float rating;

    public Review(String userID, String productKey, float rating, String comments) {
        this.userID = userID;
        this.productKey = productKey;
        this.rating = rating;
        this.comments = comments;
    }
    public Review(){}

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    private String comments;
}

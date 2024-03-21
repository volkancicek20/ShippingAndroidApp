package com.socksapp.mobileproject.model;

import com.google.firebase.Timestamp;

public class GetOffersModel {

    public static final int LAYOUT_ONE = 1;
    public static final int LAYOUT_EMPTY = 2;
    public int viewType;
    public String imageUrl;
    public String userName;
    public String number;
    public String mail;
    public String price;
    public String startCity;
    public String startDistrict;
    public String endCity;
    public String endDistrict;
    public Timestamp timestamp;

    public GetOffersModel(){

    }

    public GetOffersModel(int viewType, String imageUrl, String userName, String number, String mail, String price, String startCity, String startDistrict, String endCity, String endDistrict, Timestamp timestamp) {
        this.viewType = viewType;
        this.imageUrl = imageUrl;
        this.userName = userName;
        this.number = number;
        this.mail = mail;
        this.price = price;
        this.startCity = startCity;
        this.startDistrict = startDistrict;
        this.endCity = endCity;
        this.endDistrict = endDistrict;
        this.timestamp = timestamp;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public String getStartDistrict() {
        return startDistrict;
    }

    public void setStartDistrict(String startDistrict) {
        this.startDistrict = startDistrict;
    }

    public String getEndCity() {
        return endCity;
    }

    public void setEndCity(String endCity) {
        this.endCity = endCity;
    }

    public String getEndDistrict() {
        return endDistrict;
    }

    public void setEndDistrict(String endDistrict) {
        this.endDistrict = endDistrict;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}

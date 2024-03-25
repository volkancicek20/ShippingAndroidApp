package com.socksapp.mobileproject.model;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class GetInstitutionalModel {
    public static final int LAYOUT_ONE = 1;
    public static final int LAYOUT_EMPTY = 2;
    public int viewType;
    public String name;
    public String imageUrl;
    public String mail;
    public String number;
    public ArrayList<String> cities;

    public GetInstitutionalModel(){

    }

    public GetInstitutionalModel(int viewType, String imageUrl, String name, String number, String mail, ArrayList<String> cities) {
        this.viewType = viewType;
        this.imageUrl = imageUrl;
        this.name = name;
        this.number = number;
        this.mail = mail;
        this.cities = cities;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public ArrayList<String> getCities() {
        return cities;
    }

    public void setCities(ArrayList<String> cities) {
        this.cities = cities;
    }
}

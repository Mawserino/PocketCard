package com.example.pocketcard.model;

import android.widget.ImageView;

public class userModel {


    private String profile;
    private String name;
    private String number;
    private String email;

    public userModel(){

    }

    public userModel(String profile,String name, String number, String email){

        this.profile = profile;
        this.name = name;
        this.number = number;
        this.email = email;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}

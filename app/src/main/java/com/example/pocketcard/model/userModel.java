package com.example.pocketcard.model;

import android.widget.ImageView;

public class userModel {


    public String profile;
    public String name;
    public String number;
    public String email;
    public String auid;

    public userModel() {

    }

    public userModel(String profile, String name, String number, String email, String auid) {

        this.profile = profile;
        this.name = name;
        this.number = number;
        this.email = email;
        this.auid = auid;

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

    public String getAuid() {return auid;}

    public void setAuid(String auid) {this.auid = auid;}

}

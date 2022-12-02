package com.example.pocketcard.model;

import android.widget.ImageView;

public class rvModel {

    private String fav;
    private ImageView company;
    private String name;
    private String occupation2;
    private String email;
    private String userUID;




    public rvModel(String name, String occupation2, String userUID, String fav){

        this.name = name;
        this.occupation2 = occupation2;
        this.userUID = userUID;
        this.fav = this.fav;
    }

    public String getfav() {
        return fav;
    }

    public void setfav(String fav) {
        this.fav = fav;
    }

    public String getOccupation2() {
        return occupation2;
    }

    public void setOccupation2(String occupation2) {
        this.occupation2 = occupation2;
    }

    public ImageView getCompany() {return company;}

    public void setCompany(ImageView company) {this.company = company;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

}

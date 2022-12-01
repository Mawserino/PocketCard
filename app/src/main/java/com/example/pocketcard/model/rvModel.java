package com.example.pocketcard.model;

import android.widget.ImageView;

public class rvModel {

    private ImageView company;
    private String name;
    private String occupation;
    private String email;
    private String userUID;




    public rvModel(String name, String occupation, String email){

        this.name = name;
        this.occupation = occupation;
        this.userUID = userUID;
    }

    public ImageView getCompany() {return company;}

    public void setCompany(ImageView company) {this.company = company;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getoccupation() {
        return occupation;
    }

    public void setoccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

}

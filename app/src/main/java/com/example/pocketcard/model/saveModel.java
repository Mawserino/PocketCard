package com.example.pocketcard.model;

public class saveModel {

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAuid() {
        return auid;
    }

    public void setAuid(String auid) {
        this.auid = auid;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getNumber() {return number;}

    public void setNumber(String number) {this.number = number;}

    public saveModel(String occupation, String companyname, String location, String favorite, String auid, String name, String email, String number) {
        this.occupation = occupation;
        this.companyname = companyname;
        this.location = location;
        this.favorite = favorite;
        this.auid = auid;
        this.name = name;
        this.email = email;
        this.number = number;

    }

    private String occupation;
    private String companyname;
    private String location;


    private String favorite;
    private String auid;

    private String name;
    private String email;
    private String number;

    public saveModel(){

    }
}

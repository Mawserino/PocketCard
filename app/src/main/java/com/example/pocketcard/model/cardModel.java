package com.example.pocketcard.model;

public class cardModel {

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

    private String occupation;

    public cardModel(String occupation, String companyname, String location, String auid) {
        this.occupation = occupation;
        this.companyname = companyname;
        this.location = location;
        this.auid = auid;
    }

    private String companyname;
    private String location;
    private String auid;

    public cardModel(){

    }
}

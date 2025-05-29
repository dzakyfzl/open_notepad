package com.kelompok5.open_notepad.entity;

import java.util.Date;

public class Rate {
    private String userID;
    private String moduleID;
    private int rating;
    private Date dateRated;

    public Rate(){
        
    }

    public Rate(String userID, String moduleID, int rating) {
        this.userID = userID;
        this.moduleID = moduleID;
        this.rating = rating;
        this.dateRated = new Date(System.currentTimeMillis());
    }
    

    // Getters and Setters
    public String getUserID() {
        return userID;
    }

    public String getModuleID() {
        return moduleID;
    }

    public int getRating() {
        return rating;
    }
    public Date getDateRated() {
        return dateRated;
    }
    public void setDateRated(Date dateRated) {
        this.dateRated = dateRated;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    public void setModuleID(String moduleID) {
        this.moduleID = moduleID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }

}

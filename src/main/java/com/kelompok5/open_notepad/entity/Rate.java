package com.kelompok5.open_notepad.entity;

import java.util.Date;

public class Rate {
    private String userID;
    private String moduleID;
    private int rating;
    private Date dateRated;

    public Rate(String userID, String moduleID, int rating, Date dateRated) {
        this.dateRated = dateRated;
        this.userID = userID;
        this.moduleID = moduleID;
        this.rating = rating;
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
    // Upload To Database
    public void uploadToDatabase() {
        // Implement the logic to upload the bookmark to the database
        // This could involve using JDBC or an ORM framework like Hibernate
        // Example: DatabaseConnection.uploadBookmark(this);
    }
}

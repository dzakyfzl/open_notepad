package com.kelompok5.open_notepad.entity;

import java.sql.Date;


public class Bookmark {
    private String userID;
    private String moduleID;
    private Date dateBookmarked;

    public Bookmark(String userID, String moduleID, Date dateBookmarked) {
        this.dateBookmarked = dateBookmarked;
        this.userID = userID;
        this.moduleID = moduleID;
    }
    // Getters
    public String getUserID() {
        return userID;
    }

    public String getModuleID() {
        return moduleID;
    }

    public Date getDateBookmarked() {
        return dateBookmarked;
    }
    // Upload To Database
    public void uploadToDatabase() {
        // Implement the logic to upload the bookmark to the database
        // This could involve using JDBC or an ORM framework like Hibernate
        // Example: DatabaseConnection.uploadBookmark(this);
    }
}

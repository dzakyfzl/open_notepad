package com.kelompok5.open_notepad.entity;

import java.sql.Date;

public class View {
    private String userID;
    private String moduleID;
    private Date dateViewed;

    public View(String userID, String moduleID, Date dateViewed) {
        this.dateViewed = dateViewed;
        this.userID = userID;
        this.moduleID = moduleID;
    }
    // Getters and Setters
    public String getUserID() {
        return userID;
    }

    public String getModuleID() {
        return moduleID;
    }

    public Date getDateViewed() {
        return dateViewed;
    }
    // Upload To Database
    public void uploadToDatabase() {
        // Implement the logic to upload the bookmark to the database
        // This could involve using JDBC or an ORM framework like Hibernate
        // Example: DatabaseConnection.uploadBookmark(this);
    }
}

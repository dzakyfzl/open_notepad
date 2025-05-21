package com.kelompok5.open_notepad.DAO;

import java.sql.Date;

public class ViewDAO {
    private String userID;
    private String moduleID;
    private Date dateViewed;

    public void create(String userID, String moduleID) {
        this.userID = userID;
        this.moduleID = moduleID;
        this.dateViewed = new Date(System.currentTimeMillis());
    }
    
    public void getFromDatabase(String userID, String moduleID) {
        // Implement the logic to retrieve the view from the database
        // This could involve using JDBC or an ORM framework like Hibernate
        // Example: DatabaseConnection.getView(userID, moduleID);
    }
    public void uploadToDatabase() {
        // Implement the logic to upload the rate to the database
        // This could involve using JDBC or an ORM framework like Hibernate
        // Example: DatabaseConnection.uploadRate(this);
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

}

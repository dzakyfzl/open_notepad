package com.kelompok5.open_notepad.DAO;

import java.util.Date;

public class RateDAO {
    private String userID;
    private String moduleID;
    private int rating;
    private Date dateRated;

    public void create(String userID, String moduleID, int rating) {
        this.userID = userID;
        this.moduleID = moduleID;
        this.rating = rating;
        this.dateRated = new Date(System.currentTimeMillis());
    }
    
    public void getFromDatabase(String userID, String moduleID) {
        // Implement the logic to retrieve the rate from the database
        // This could involve using JDBC or an ORM framework like Hibernate
        // Example: DatabaseConnection.getRate(userID, moduleID);
    }
    public void uploadToDatabase() {
        // Implement the logic to upload the rate to the database
        // This could involve using JDBC or an ORM framework like Hibernate
        // Example: DatabaseConnection.uploadRate(this);
    }
    public void deleteFromDatabase() {
        // Implement the logic to delete the rate from the database
        // This could involve using JDBC or an ORM framework like Hibernate
        // Example: DatabaseConnection.deleteRate(this);
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

}

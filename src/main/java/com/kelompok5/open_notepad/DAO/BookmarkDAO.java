package com.kelompok5.open_notepad.DAO;

import java.sql.Date;


public class BookmarkDAO {
    private String userID;
    private String moduleID;
    private Date dateBookmarked;

    public void create(String userID, String moduleID) {
        this.userID = userID;
        this.moduleID = moduleID;
        this.dateBookmarked = new Date(System.currentTimeMillis());
    }

    public void getFromDatabase(String userID, String moduleID) {
        // Implement the logic to retrieve the bookmark from the database
        // This could involve using JDBC or an ORM framework like Hibernate
        // Example: DatabaseConnection.getBookmark(userID, moduleID);
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

}

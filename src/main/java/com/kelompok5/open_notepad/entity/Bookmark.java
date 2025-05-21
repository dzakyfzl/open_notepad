package com.kelompok5.open_notepad.entity;

import java.sql.Date;


public class Bookmark {
    private String userID;
    private String moduleID;
    private Date dateBookmarked;

    public void create(String userID, String moduleID) {
        this.userID = userID;
        this.moduleID = moduleID;
        this.dateBookmarked = new Date(System.currentTimeMillis());
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

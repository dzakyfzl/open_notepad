package com.kelompok5.open_notepad.entity;

import java.sql.Date;


public class Bookmark {
    private String userID;
    private int moduleID;
    private Date dateBookmarked;

    public void create(String userID, int moduleID) {
        this.userID = userID;
        this.moduleID = moduleID;
        this.dateBookmarked = new Date(System.currentTimeMillis());
    }

    // Getters
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }


    public int getModuleID() {
        return moduleID;
    }
    public void setModuleID(int moduleID) {
        this.moduleID = moduleID;
    }


    public Date getDateBookmarked() {
        return dateBookmarked;
    }
    public void setDateBookmarked(Date dateBookmarked) {
        this.dateBookmarked = dateBookmarked;
    }

}

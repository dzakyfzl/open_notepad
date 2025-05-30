package com.kelompok5.open_notepad.entity;

import java.sql.Date;

public class View {
    private String userID;
    private int moduleID;
    private Date dateViewed;

    public void create(String userID, int moduleID) {
        this.userID = userID;
        this.moduleID = moduleID;
        this.dateViewed = new Date(System.currentTimeMillis());
    }

    
    // Getters and Setters
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



    public Date getDateViewed() {
        return dateViewed;
    }
    public void setDateViewed(Date dateViewed) {
        this.dateViewed = dateViewed;
    }

}

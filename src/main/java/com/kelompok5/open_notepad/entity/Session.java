package com.kelompok5.open_notepad.entity;

import java.sql.Date;

public class Session {
    private String sessionID;
    private String username;
    private String userAgent;
    private Date dateCreated;

    public Session(String sessionID, String username, String userAgent, Date dateCreated) {
        this.sessionID = sessionID;
        this.username = username;
        this.userAgent = userAgent;
        this.dateCreated = dateCreated;
    }

    // Getters and Setters
    public String getSessionID() {
        return sessionID;
    }
    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUserAgent() {
        return userAgent;
    }
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    public Date getDateCreated() {
        return dateCreated;
    }
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}

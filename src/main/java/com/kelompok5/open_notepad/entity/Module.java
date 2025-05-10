package com.kelompok5.open_notepad.entity;

import java.sql.Date;

public class Module {
    private final String moduleID;
    private final String ownerID;
    private String title;
    private String description;
    private String course;
    private Date uploadDate;
    private boolean visibility;
    private final  String file;

    public Module(String moduleID, String ownerID, String title, String description, String course, Date uploadDate, boolean visibility, String file) {
        this.moduleID = moduleID;
        this.ownerID = ownerID;
        this.title = title;
        this.description = description;
        this.course = course;
        this.uploadDate = uploadDate;
        this.visibility = visibility;
        this.file = file;
    }
    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }
    public void uploadToDatabase() {
        //upload module to database logic
    }
    //getter
    public String getModuleID() {
        return moduleID;
    }
    public String getOwnerID() {
        return ownerID;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getCourse() {
        return course;
    }
    public Date getUploadDate() {
        return uploadDate;
    }
    public boolean isVisibility() {
        return visibility;
    }
    public String getFile() {
        return file;
    }

    //setter
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setCourse(String course) {
        this.course = course;
    }
    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }


}

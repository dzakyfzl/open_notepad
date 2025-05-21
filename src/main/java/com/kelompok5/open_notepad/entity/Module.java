package com.kelompok5.open_notepad.entity;

import java.sql.Date;

public class Module {
    private String moduleID;
    private String ownerID;
    private String title;
    private String description;
    private String course;
    private String major;
    private Date uploadDate;
    private boolean visibility;
    private File file;

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
    public File getFile() {
        return file;
    }
    public String getMajor() {
        return major;
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
    public void setMajor(String major) {
        this.major = major;
    }
    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }


}

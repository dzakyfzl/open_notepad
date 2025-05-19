package com.kelompok5.open_notepad.DAO;


import java.sql.Date;

import com.kelompok5.open_notepad.entity.File;

public class ModuleDAO {
    private String moduleID;
    private String ownerID;
    private String title;
    private String description;
    private String course;
    private String major;
    private Date uploadDate;
    private boolean visibility;
    private File file;

    public void getFromDatabase(String moduleID){
        this.file = new File(rs.getString("fileID"), rs.getString("name"), rs.getString("type"), rs.getBytes("size"), rs.getString("path"));
        //get module from database logic
        

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

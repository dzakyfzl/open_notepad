package com.kelompok5.open_notepad.entity;

public class File {

    public File(int fileID, String name, String type, long size, String path) {
        this.fileID = fileID;
        this.name = name;
        this.type = type;
        this.size = size;
        this.path = path;
    }
    private int fileID;
    private String name;
    private String path;
    private String type;
    private long size;
    
    public void GetFromDatabase(String fileID) {
        // Implement database logic to retrieve file information
        // and populate the fields accordingly
    }
    public void UploadToDatabase() {
        // Implement database logic to save file information
    }

    //getter and setter methods
    public int getFileID() {
        return fileID;
    }
    public void setFileID(int fileID) {
        this.fileID = fileID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public long getSize() {
        return size;
    }
    public void setSize(long size) {
        this.size = size;
    }

}

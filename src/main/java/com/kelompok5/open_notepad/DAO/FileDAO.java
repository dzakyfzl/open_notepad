package com.kelompok5.open_notepad.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.kelompok5.open_notepad.entity.File;

@Component
public class FileDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    public int getFileID(String filename) {
        // Implement database logic to retrieve file ID based on filename
        String sql = "SELECT fileID FROM Files WHERE name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{filename}, Integer.class);
        } catch (Exception e) {
            // Handle exception, e.g., file not found
            return -1; // or throw an exception
        }
    }
    public File GetFromDatabase(int  fileID) {
        // Implement database logic to retrieve file information
        // and populate the fields accordingly
        String sql = "SELECT * FROM Files WHERE fileID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{fileID}, (rs, rowNum) -> {
                return new File(
                    rs.getInt("fileID"),
                    rs.getString("name"),
                    rs.getString("type"),
                    rs.getLong("size"),
                    rs.getString("path")
                );
            });
        } catch (Exception e) {
            // Handle exception
            return null;
        }
    }
    public void UploadToDatabase(File file) { 
        // Implement database logic to save file information
        String sql = "INSERT INTO Files(name,type,size,path) VALUES (?,?,?,?)";
        try {
            jdbcTemplate.update(sql,new Object[]{file.getName(),file.getType(),file.getSize(),file.getPath()});
        }catch (Exception e){
            System.out.println("Error uploading file to database: " + e.getMessage());
            throw new RuntimeException("failed to upload file property to database");
        }

    }
    public void DeleteFromDatabase(int fileID) {
        // Implement database logic to delete file information
        String sql = "DELETE FROM Files WHERE fileID = ?";
        try {
            jdbcTemplate.update(sql, fileID);
        } catch (Exception e) {
            System.out.println("Error deleting file from database: " + e.getMessage());
            throw new RuntimeException("failed to delete file from database");
        }
    }
    

}

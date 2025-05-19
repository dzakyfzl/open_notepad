package com.kelompok5.open_notepad.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.kelompok5.open_notepad.entity.File;

@Component
public class FileDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public File GetFromDatabase(String fileID) {
        // Implement database logic to retrieve file information
        // and populate the fields accordingly
        String sql = "SELECT * FROM Files WHERE fileID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{fileID}, (rs, rowNum) -> {
                return new File(
                    rs.getString("fileID"),
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
        String sql = "INSERT INTO Files(name,type,size,path) VALUE (?,?,?,?)";
        try {
            jdbcTemplate.update(sql,new Object[]{file.getName(),file.getType(),file.getSize(),file.getPath()});
        }catch (Exception e){
            throw new RuntimeException("failed to upload file property to database");
        }

    }

}

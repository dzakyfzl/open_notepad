package com.kelompok5.open_notepad.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.kelompok5.open_notepad.entity.Bookmark;

@Component
public class BookmarkDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public int getFromUser(String userID) {
        String sql = "SELECT COUNT(*) AS bookmark FROM Bookmarks WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{userID},(rs,rowNum)-> rs.getInt("bookmark"));
            
        }catch(Exception e){
            return -1;
        }
    }
    public int getFromNote(int moduleID) {
        String sql = "SELECT COUNT(*) AS bookmark FROM Bookmarks WHERE moduleID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{moduleID},(rs,rowNum)-> rs.getInt("bookmark"));
             
        }catch(Exception e){
            return -1;
        }
        
    }
    public void uploadToDatabase(Bookmark bookmark) {
        // Implement the logic to upload the bookmark to the database
        // This could involve using JDBC or an ORM framework like Hibernate
        // Example: DatabaseConnection.uploadBookmark(this);
    }
    public void deleteFromUser(String userID) {
        // Implement the logic to delete the bookmark from the database
        // This could involve using JDBC or an ORM framework like Hibernate
        // Example: DatabaseConnection.deleteBookmark(this);
    }
        public void deleteFromModule(int noteID) {
        // Implement the logic to delete the bookmark from the database
        // This could involve using JDBC or an ORM framework like Hibernate
        // Example: DatabaseConnection.deleteBookmark(this);
    }

}

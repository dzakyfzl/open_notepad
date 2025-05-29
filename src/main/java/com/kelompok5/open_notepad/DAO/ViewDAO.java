package com.kelompok5.open_notepad.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.kelompok5.open_notepad.entity.View;

@Component
public class ViewDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public int getFromUser(String userID) {
        String sql = "SELECT COUNT(*) AS views FROM Views WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{userID},(rs,rowNum)-> rs.getInt("views"));
             
        }catch(Exception e){
            System.out.println("Error get view from user "+e.getMessage());
            return -1;
        }
    }
    public int getFromNote(int moduleID) {
        var sql = "SELECT COUNT(*) AS views FROM Views WHERE moduleID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{moduleID},(rs,rowNum)-> rs.getInt("views"));
             
        }catch(Exception e){
            System.out.println("Error get view from note "+e.getMessage());
            return -1;
        }
        
    }
    public void uploadToDatabase(View view) {
        // Implement the logic to upload the views to the database
        // This could involve using JDBC or an ORM framework like Hibernate
        // Example: DatabaseConnection.uploadRate(this);
    }
    public void deleteFromUser(String userID) {
        // Implement the logic to delete the views from the database
        // This could involve using JDBC or an ORM framework like Hibernate
        // Example: DatabaseConnection.deleteRate(this);
    }
        public void deleteFromModule(int noteID) {
        // Implement the logic to delete the views from the database
        // This could involve using JDBC or an ORM framework like Hibernate
        // Example: DatabaseConnection.deleteRate(this);
    }

}

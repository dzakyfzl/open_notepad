package com.kelompok5.open_notepad.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.kelompok5.open_notepad.entity.Rate;

@Component
public class RateDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public float getFromUser(String userID) {
        String sql = "SELECT AVG(rating) AS rate FROM Ratings WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{userID},(rs,rowNum)-> rs.getFloat("rate"));
             
        }catch(Exception e){
            System.out.println("get rate from user : "+e.getMessage());
            return -1;
        }
    }
    public float getFromNote(int moduleID) {
        String sql = "SELECT AVG(rating) AS rate FROM Ratings WHERE moduleID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{moduleID},(rs,rowNum)-> rs.getFloat("rate"));
             
        }catch(Exception e){
            System.out.println("get rate from module : "+e.getMessage());
            return -1;
        }
        
    }
    public void uploadToDatabase(Rate rate) {
        // Implement the logic to upload the rate to the database
        // This could involve using JDBC or an ORM framework like Hibernate
        // Example: DatabaseConnection.uploadRate(this);
    }
    public void deleteFromUser(String userID) {
        // Implement the logic to delete the rate from the database
        // This could involve using JDBC or an ORM framework like Hibernate
        // Example: DatabaseConnection.deleteRate(this);
    }
        public void deleteFromModule(int noteID) {
        // Implement the logic to delete the rate from the database
        // This could involve using JDBC or an ORM framework like Hibernate
        // Example: DatabaseConnection.deleteRate(this);
    }

}

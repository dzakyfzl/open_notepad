package com.kelompok5.open_notepad.DAO;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

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
    public void uplaodToDatabase(String username, int noteID){
        String sql = "INSERT INTO Views(username, moduleID, dateViewed) VALUES (?,?,?)";
        Date timestamp = new Date(System.currentTimeMillis());
        jdbcTemplate.update(sql, username,noteID,timestamp);
    }
    public void deleteFromUser(String userID) {
        String sql = "DELETE FROM Views WHERE username = ?";
        jdbcTemplate.update(sql, userID);
    }
    public void deleteFromNote(int moduleID) {
        String sql = "DELETE FROM Views WHERE moduleID = ?";
        jdbcTemplate.update(sql, moduleID);
    }

}

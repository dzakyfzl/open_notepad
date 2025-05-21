package com.kelompok5.open_notepad.DAO;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.kelompok5.open_notepad.entity.Session;

@Component
public class SessionDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void uploadToDatabase(String sessionID, String username, String userAgent) {
        //set session logic
        //Querry inserting to database
        String sql = "INSERT INTO Sessions (sessionID, username, userAgent, dateCreated) VALUES (?, ?, ?, ?)";
        try {
            // Get the current timestamp in SQL format
            Date timestamp = new Date(System.currentTimeMillis());
            // Insert the session data into the database
            jdbcTemplate.update(sql, sessionID, username, userAgent, timestamp);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload session data to the database");
            
        }

    }

    public void deleteSession(String username) {
        //Querry deleting from database
        String sql = "DELETE FROM Sessions WHERE username = ?";
        try {
            // Delete the session data from the database
            jdbcTemplate.update(sql, username);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete session data from the database");
        }
    }
    public Session getFromDatabase(String username) {
        //set session logic
        //Querry inserting to database
        String sql = "SELECT sessionID, username, userAgent, dateCreated  FROM Sessions WHERE username = ?";
        try {
            // Insert the session data into the database
            return jdbcTemplate.queryForObject(sql, new Object[]{username},
                    (rs, rowNum) -> new Session(
                            rs.getString("sessionID"),
                            rs.getString("username"),
                            rs.getString("userAgent"),
                            rs.getDate("dateCreated")
                    ));
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload session data to the database");
        }

    }

    public void deleteExpiredSessions(){
        //Querry deleting from database that more than 1 day
        String sql = "DELETE FROM Sessions WHERE dateCreated < ?";
        try {
            // Delete the session data from the database
            Date now = new Date(System.currentTimeMillis());
            Date expiredDate = new Date(now.getTime() - 86400000); // 1 day
            jdbcTemplate.update(sql, expiredDate);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete expired session data from the database");
        }
    }
}

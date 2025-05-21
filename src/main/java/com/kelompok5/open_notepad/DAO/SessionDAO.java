package com.kelompok5.open_notepad.DAO;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SessionDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void uploadToDatabase(String sessionID, String username, String IPString, String userAgent) {
        //set session logic
        //Querry inserting to database
        String sql = "INSERT INTO Sessions (sessionID, username, IPAddress, userAgent, dateCreated) VALUES (?, ?, ?, ?, ?)";
        try {
            // Get the current timestamp in SQL format
            Date timestamp = new Date(System.currentTimeMillis());
            // Insert the session data into the database
            jdbcTemplate.update(sql, sessionID, username, IPString, userAgent, timestamp);
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
}

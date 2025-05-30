package com.kelompok5.open_notepad.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

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
    public boolean get(String userID, int moduleID) {
        String sql = "SELECT * FROM Bookmarks WHERE username = ? AND moduleID = ?";
        try {
            jdbcTemplate.queryForObject( sql, new Object[]{userID,moduleID},(rs,rowNum)-> rs.getInt("moduleID"));
            return true;
        }catch(EmptyResultDataAccessException e){
            return false;
        }
    }
    
    public void uplaodToDatabase(String username, int noteID){
        String sql = "INSERT INTO Bookmarks(moduleID, username, dateBookmarked) VALUE (?,?,NOW)";
        jdbcTemplate.update(sql, noteID, username);
    }
    public void deleteFromUser(String userID) {
        String sql = "DELETE FROM Bookmarks WHERE username = ?";
        jdbcTemplate.update(sql, userID);
    }
    public void deleteFromNote(int moduleID) {
        String sql = "DELETE FROM Bookamarks WHERE moduleID = ?";
        jdbcTemplate.update(sql, moduleID);
    }

}

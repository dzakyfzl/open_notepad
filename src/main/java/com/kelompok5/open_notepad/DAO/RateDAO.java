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
    public Rate getRate(String username, int noteID){
        String sql = "SELECT * FROM Ratings WHERE username = ? AND moduleID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{username,noteID},(rs,rowNum)->{
                Rate rate = new Rate();
                rate.setRating(rs.getInt("rating"));
                rate.setUserID(rs.getString("username"));
                rate.setModuleID(rs.getInt("moduleID"));
                rate.setDateRated(rs.getDate("dateRated"));
                return rate;
            });
        }catch (Exception e){
            System.out.println("get rate : "+e.getMessage());
            return null;
        }
    }

    public void uplaodToDatabase(Rate rating){
        String sql = "INSERT INTO Ratings(moduleID, username, rating, dateRated) VALUES (?,?,?,?)";
        jdbcTemplate.update(sql, rating.getModuleID(), rating.getUserID(), rating.getRating(), rating.getDateRated());
    }
    public void deleteFromUser(String userID) {
        String sql = "DELETE FROM Ratings WHERE username = ?";
        jdbcTemplate.update(sql, userID);
    }
    public void deleteFromNote(int moduleID) {
        String sql = "DELETE FROM Ratings WHERE moduleID = ?";
        jdbcTemplate.update(sql, moduleID);
    }

}

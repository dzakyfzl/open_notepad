package com.kelompok5.open_notepad.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.kelompok5.open_notepad.entity.Admin;

@Component
public class AdminDAO extends AccountDAO {

    @Autowired
    private JdbcTemplate jbdcTemplate;
    
    public void uploadToDatabase(Admin admin) {
        //upload user to database logic
        //save user to database
    }

    public Admin getFromDatabase(String username){
        //get user from database logic
        // Query the database to check if the user exists
        String sql = "SELECT * FROM Accounts WHERE username = ?";
        try {
            return jbdcTemplate.queryForObject(sql, new Object[]{username}, (rs, rowNum) -> {
                return new Admin(
                    rs.getString("username"),
                    rs.getString("hashedPassword"),
                    rs.getString("salt"),
                    rs.getString("email"), 
                    rs.getString("firstName"), 
                    rs.getString("lastName"));
            });
        } catch (EmptyResultDataAccessException e) {
            // If the user is not found, return an error message
            return null;
        }
    }
    
    public void deleteModule(Module module) {
        //delete module logic
    }
    public void setStatusModule(Module module) {
        //set status module logic
    }

    public void deleteAccount(String username, String hashedPassword) {
        String sql = "SELECT hashedPassword FROM Accounts WHERE username = ?";
        try {
            String storedHashed = jdbcTemplate.queryForObject(sql, new Object[] { username }, String.class);

            if (!storedHashed.equals(hashedPassword)) {
                throw new RuntimeException("Wrong password");
            }

            // Hapus session user dari tabel Sessions
            String deleteSessionSql = "DELETE FROM Sessions WHERE username = ?";
            jdbcTemplate.update(deleteSessionSql, username);

            // Hapus akun
            sql = "DELETE FROM Accounts WHERE username = ?";
            int rows = jdbcTemplate.update(sql, username);
            if (rows == 0) {
                throw new RuntimeException("User not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user: " + e.getMessage());
        }
    }
}

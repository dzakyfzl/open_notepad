package com.kelompok5.open_notepad.DAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.kelompok5.open_notepad.entity.Module;


@Component
public abstract  class AccountDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;



    public void register(String username, String hashedPassword, String salt, String email, String firstName, String lastName){
        //registration logic
        // Query the database to check if the user exists
        int rowCount = 0;
        String sql = "SELECT COUNT(*) AS Account FROM Accounts WHERE username = ?";
        rowCount = jdbcTemplate.queryForObject(sql, new Object[]{username}, (rs, rowNum) -> rs.getInt("Account"));
        if (rowCount > 0) {
            // If the user already exists, return an error message
            throw new RuntimeException("user already exist");
        }
        //save to database
        sql = "INSERT INTO Accounts (username, hashedPassword, salt, email, firstName, lastName) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql, username, hashedPassword, salt, email, firstName, lastName);
        } catch (Exception e) {
            // If there is an error during registration, return an error message
            throw new RuntimeException("error uploading to database");
        }
    }

    public void updateInfo(String username, String email, String firstName, String lastName, String hashedPassword, String salt) {
        //update user info logic
        //save to database
        String sql = "UPDATE Accounts SET email = ?, firstName = ?, lastName = ?, hashedPassword = ?, salt = ? WHERE username = ?";
        try {
            jdbcTemplate.update(sql, email, firstName, lastName, hashedPassword, salt, username);
        } catch (Exception e) {
            // If there is an error during update, return an error message
            throw new RuntimeException("Error updating user info");
        }
    }

   public void delete(String username, String password) {
    // Ambil hashedPassword dari database
    String sql = "SELECT hashedPassword FROM Accounts WHERE username = ?";
    try {
        String hashedPassword = jdbcTemplate.queryForObject(sql, new Object[]{username}, String.class);
        // Bandingkan password (asumsi password sudah di-hash sebelum dikirim ke method ini)
        if (!hashedPassword.equals(password)) {
            throw new RuntimeException("Wrong password");
        }
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

    //Modules Interaction
    public List<Module> findModule(String keyParams) {
        //find module logic
        //search module in database
        return null;
    }
    public void deleteModule(Module module) {
        //delete module logic
        //remove module from database
    }

    

}

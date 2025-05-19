package com.kelompok5.open_notepad.DAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.kelompok5.open_notepad.Security;
import com.kelompok5.open_notepad.entity.Admin;
import com.kelompok5.open_notepad.entity.Module;
import com.kelompok5.open_notepad.entity.User;


@Component
public abstract  class AccountDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Security security;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private AdminDAO adminDAO;


    public void login(String username, String password) {
        //login logic
        // Query the database to check if the account is admin or not
        String sql = "SELECT isAdmin FROM Accounts WHERE username = ?";
        boolean isAdmin;
        try {
            isAdmin = jdbcTemplate.queryForObject(sql, new Object[]{username},Boolean.class);
        } catch (Exception e) {
            throw new RuntimeException("failed to get isAdmin from database");
        }

        //if statement
        if(isAdmin){
            Admin admin = adminDAO.getFromDatabase(username);
            if (admin == null) {
                // If the user is not found, return an error message
                throw new RuntimeException("User not found");
            }
            // Check if the password matches
            if (security.passwordIsValid(password, admin.getHashedPassword(), admin.getSalt())) {
                // If the password does not match, return an error message
                throw new RuntimeException("Invalid password");
            }
        }else{
            User user = userDAO.getFromDatabase(username);
            if (user == null) {
                // If the user is not found, return an error message
                throw new RuntimeException("User not found");
            }
            // Check if the password matches
            if (security.passwordIsValid(password, user.getHashedPassword(), user.getSalt())) {
                // If the password does not match, return an error message
                throw new RuntimeException("Invalid password");
            }
        }
    }

    public void register(String username, String hashedPassword, String salt, String email, String firstName, String lastName){
        //registration logic
        // Query the database to check if the user exists
        String sql = "SELECT * FROM Accounts WHERE username = ?";
        int rowCount = jdbcTemplate.queryForObject(sql, new Object[]{username}, Integer.class);
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

    public void delete() {
        //delete account logic
        //remove all data from database
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

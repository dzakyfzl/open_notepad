package com.kelompok5.open_notepad.DAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.kelompok5.open_notepad.entity.User;

@Component
public class UserDAO extends AccountDAO{

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public void uploadModule(Module module) {
        //upload module logic
        //save module to database
    }
    public Module downloadModule(Module module) {
        //download module logic
        //get module from database
        //return module
        return null;
    }
    public void editModule(Module module) {
        //edit module logic
        //check if module is owned by user
        //update module in database

    }
    public List<Module> showModules() {
        //get module list logic
        return null;
    }
    public void editDetails(String username,String aboutMe, String instagram, String linkedin) {
        String sql = "INSERT INTO UserDetails(username,,aboutMe,instagram,linkedin) VALUE (?,?,?,?)";
        try {
            jdbcTemplate.update(sql,username,aboutMe,instagram,linkedin);
        } catch (Exception e) {
            throw new RuntimeException("failed to inserting details");
        }
    }

    public void deleteModule(Module module) {
        //delete module logic
        //check if module is owned by user
        //remove module from database
    }

    public void uploadToDatabase(User user) {
        //upload user to database logic
        //save user to database
    }

    public User getFromDatabase(String username){
        //get user from database logic
        // Query the database to check if the user exists
        String sql = "SELECT * FROM Accounts WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{username}, (rs, rowNum) -> {
                return new User(
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
    
}

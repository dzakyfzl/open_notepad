package com.kelompok5.open_notepad.DAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.kelompok5.open_notepad.entity.Note;
import com.kelompok5.open_notepad.entity.User;

@Component
public class UserDAO extends AccountDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void uploadNote(Note note) {
        // upload note logic
        // save note to database
    }

    public Note downloadNote(Note note) {
        // download note logic
        // get note from database
        // return note
        return null;
    }

    public void editNote(Note note) {
        // edit note logic
        // check if note is owned by user
        // update note in database

    }

    public List<Note> showNotes() {
        // get note list logic
        return null;
    }

    public void createUserDetails(String username) {
        // create user details logic
        // save user details to database
        String sql = "INSERT INTO UserDetails(username) VALUES (?)";
        try {
            jdbcTemplate.update(sql, username);
        } catch (Exception e) {
            throw new RuntimeException("failed to creating user details");
        }
    }

    public void editDetails(String username, String aboutMe, String instagram, String linkedin) {
        String sql = "INSERT INTO UserDetails(username,aboutMe,instagram,linkedin) VALUE (?,?,?,?)";
        try {
            jdbcTemplate.update(sql, username, aboutMe, instagram, linkedin);
        } catch (Exception e) {
            throw new RuntimeException("failed to inserting details");
        }
    }

    public void deleteNote(Note note) {
        // delete note logic
        // check if note is owned by user
        // remove note from database
    }


    public User getFromDatabase(String username) {
        // get user from database logic
        // Query the database to check if the user exists
        String sql = "SELECT * FROM Accounts INNER JOIN UserDetails ON Accounts.username = UserDetails.username WHERE Accounts.username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[] { username }, (rs, rowNum) -> {
                return new User(
                        rs.getString("username"),
                        rs.getString("hashedPassword"),
                        rs.getString("salt"),
                        rs.getString("email"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("aboutMe"),
                        rs.getString("instagram"),
                        rs.getString("linkedin")
                );
            });
        } catch (Exception e) {
            // If the user is not found, return an error message
            System.out.println("User not found: " + e.getMessage());
            return null;
        }
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

package com.kelompok5.open_notepad.DAO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.kelompok5.open_notepad.entity.File;
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
        String sql = "UPDATE UserDetails SET aboutMe = ?, instagram = ?, linkedin = ? WHERE username = ?";
        try {
            jdbcTemplate.update(sql, aboutMe, instagram, linkedin, username );
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
                String aboutMe= rs.getString("aboutMe");
                String instagram= rs.getString("instagram"); 
                String linkedin = rs.getString("linkedin");

                // null prevention in aboutMe, Instagram and Inkedin
                if (rs.getString("aboutMe").equals(",")) {
                    aboutMe = "";
                }
                if (rs.getString("instagram").equals(",")) {
                    instagram = "";
                }
                if (rs.getString("linkedin").equals(",")) {
                    linkedin = "";
                }

                return new User(
                        rs.getString("username"),
                        rs.getString("hashedPassword"),
                        rs.getString("salt"),
                        rs.getString("email"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        aboutMe,
                        instagram,
                        linkedin
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

            // Ambil Data catatan yang terkait dengan akun
            String notesSql = "SELECT * FROM Files INNER JOIN Notes on Notes.fileID = Files.fileID WHERE Notes.username = ?";
            List<File> files = jdbcTemplate.query(notesSql, new Object[] { username }, (rs, rowNum) -> {
                File file = new File(
                        rs.getInt("fileID"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getLong("size"),
                        rs.getString("path")
                );
                return file;
            });
            for (File file : files) {
                // Hapus file di server
                Path oldFilePath = Paths.get(file.getPath());
                try {
                    Files.deleteIfExists(oldFilePath);
                } catch (IOException e) {
                    System.out.println("Error deleting file: " + e.getMessage());
                    throw new RuntimeException("Failed to delete file");
                }
            }
            
            // Hapus catatan yang terkait dengan akun
            String deleteNotesSql = "DELETE FROM Notes WHERE username = ?";
            jdbcTemplate.update(deleteNotesSql, username);

            //Ambil ID file foto profil dari Accounts
            sql = "SELECT fileID FROM Accounts WHERE username = ?";
            Integer fileID = jdbcTemplate.queryForObject(sql, new Object[] {username}, Integer.class);
            sql = "UPDATE Accounts SET fileID = NULL WHERE username = ?";
            try {
                jdbcTemplate.update(sql, username);
            } catch (Exception e) {
                System.out.println("Error updating fileID: " + e.getMessage());
                throw new RuntimeException("Failed to update fileID");
            }
            

           

            if (fileID != null) {
                // Ambil data foto profil dari tabel Files
                String profilePhotoSql = "SELECT * FROM Files WHERE fileID = ?";
                File profilePhoto = jdbcTemplate.queryForObject(profilePhotoSql, new Object[] { fileID }, (rs, rowNum) -> {
                    return new File(rs.getInt("fileID"), rs.getString("name"), rs.getString("type"), rs.getLong("size"), rs.getString("path"));
                });
                // Hapus file foto profil di server
                Path oldFilePath = Paths.get(profilePhoto.getPath());
                try {
                    Files.deleteIfExists(oldFilePath);
                } catch (IOException e) {
                    System.out.println("Error deleting profile photo file: " + e.getMessage());
                    throw new RuntimeException("Failed to delete profile photo file");
                }

                 // Hapus data file foto profil di database
                String deleteProfilePicSql = "DELETE FROM Files WHERE fileID = ?";
                jdbcTemplate.update(deleteProfilePicSql, fileID);
            }
            

            // Hapus session user dari tabel Sessions
            String deleteSessionSql = "DELETE FROM Sessions WHERE username = ?";
            jdbcTemplate.update(deleteSessionSql, username);

            // Hapus detail user dari tabel UserDetails
            String deleteDetailsSql = "DELETE FROM UserDetails WHERE username = ?";
            jdbcTemplate.update(deleteDetailsSql, username);


            // Hapus akun
            sql = "DELETE FROM Accounts WHERE username = ?";
            int rows = jdbcTemplate.update(sql, username);
            if (rows == 0) {
                throw new RuntimeException("User not found");
            }
        } catch (Exception e) {
            System.out.println("Error deleting account: " + e.getMessage());
            throw new RuntimeException("Failed to delete account");
        }
    }
}

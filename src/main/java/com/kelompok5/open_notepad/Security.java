package com.kelompok5.open_notepad;

import java.security.MessageDigest;
import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.kelompok5.open_notepad.DAO.SessionDAO;
import com.kelompok5.open_notepad.entity.Session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Component
public class Security{

    @Autowired
    private SessionDAO sessionDAO;

    @Autowired
    private JdbcTemplate jdbctemplate;

    public boolean isSessionValid(HttpSession session, HttpServletRequest request) {
        String sessionID = session.getId();
        String username = (String) session.getAttribute("username");
        String UserAgent = request.getHeader("User-Agent");
        //Querry from database using SessionDAO
        Session savedSession; 
        try {
            sessionDAO.deleteExpiredSessions();
            savedSession = sessionDAO.getFromDatabase(username);
        } catch (Exception e) {
            return false;
        }
        // Return true if the sessionID and UserAgent match
        if (savedSession == null) {
            return false;
        }
        return sessionID.equals(savedSession.getSessionID()) && UserAgent.equals(savedSession.getUserAgent());
    }

    public boolean isAdmin(HttpSession session) {
        // Check if the user has admin privileges
        String sql = "SELECT isAdmin FROM Accounts WHERE username = ?";
        return jdbctemplate.queryForObject(sql, Boolean.class, session.getAttribute("username"));

    }

    public String generateSalt() {
        // Implement your salt generation logic here (e.g., using SecureRandom)
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16]; // 128-bit salt
        random.nextBytes(salt);
        StringBuilder hexString = new StringBuilder();
        for (byte b : salt) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public  String hashPassword(String password, String salt) {
        // Combine password and salt before hashing
        // Implement your hashing logic here (e.g., using SHA256)
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String saltedPassword = password + salt;
            byte[] hash = digest.digest(saltedPassword.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean passwordIsValid(String password, String hashedPassword, String salt) {
        String hashedInputPassword = hashPassword(password, salt);
        return hashedInputPassword.equals(hashedPassword);
    }
}

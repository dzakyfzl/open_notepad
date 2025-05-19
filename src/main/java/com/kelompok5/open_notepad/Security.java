package com.kelompok5.open_notepad;

import java.security.MessageDigest;
import java.security.SecureRandom;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Component
public class Security{
    protected boolean isSessionValid(HttpSession session, HttpServletRequest request) {
        String sessionID = "000";
        String IPString = request.getRemoteAddr();
        //Querry IP from database

        return session == null;  //Delete if debugging done
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

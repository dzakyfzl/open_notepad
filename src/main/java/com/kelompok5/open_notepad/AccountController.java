package com.kelompok5.open_notepad;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kelompok5.open_notepad.entity.File;
import com.kelompok5.open_notepad.entity.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;






@RestController
@RequestMapping("/api/account")
public class AccountController{
    
    private final Security security;
    public AccountController(Security security) {
        this.security = security;
    }
    @Autowired
    private JdbcTemplate jdbcTemplate;
    //Authentication API
    @PostMapping("/signin")//login
    public ResponseEntity<Map<String,String>> signIn(@RequestBody Map<String,String> requestData,HttpServletRequest request, HttpSession session) {
        String username = requestData.get("username");
        String password = requestData.get("password");
        if(security.isSessionValid(session, request)) {
            return ResponseEntity.badRequest().body(Map.of("message", "User already logged in"));
        }
        String IPString = request.getRemoteAddr();
        String sessionID = session.getId();
        session.setMaxInactiveInterval(60*60*24); // 1 day
        if(username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Username and password are required"));
        }
        //Check if the user exists in the database
        String sql = "SELECT * FROM Accounts WHERE username = ?";
        User user = null;
        try {
            user = jdbcTemplate.queryForObject(sql, new Object[]{username}, (rs, rowNum) -> {
                return new User(rs.getString("username"), rs.getString("hashedPassword"), rs.getString("salt"), rs.getString("email"), rs.getString("firstName"), rs.getString("lastName"));
            });

        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not found"));
        }

    // ✅ Verifikasi password
        String hashedInputPassword = security.hashPassword(password, user.getSalt());
        if (!hashedInputPassword.equals(user.getHashedPassword())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid password"));
        }

    // ✅ Login sukses
        session.setMaxInactiveInterval(60 * 60 * 24);
        session.setAttribute("username", username);   
        return ResponseEntity.ok().body(Map.of("message", "User logged in successfully"));
    }

    @PostMapping("/signup")//register
    public ResponseEntity<Map<String,String>> signUp(@RequestBody Map<String,String> requestData,HttpServletRequest request, HttpSession session) {
        String username = requestData.get("username");
        String password = requestData.get("password");
        String email = requestData.get("email");
        String firstName = requestData.get("firstName");
        String lastName = requestData.get("lastName");
        if(security.isSessionValid(session, request)) {
            return ResponseEntity.badRequest().body(Map.of("message", "User already logged in"));
        }
        if(username == null || password == null || email == null || firstName == null || lastName == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "All fields are required"));
        }
        //Hash password and salt it
        String salt = security.generateSalt();
        String hashedPassword = security.hashPassword(password, salt);
        //Create User object
        
        User user = new User(username, hashedPassword, salt, email, firstName, lastName);
        jdbcTemplate.update(
            "INSERT INTO Accounts (username, hashedPassword, salt, email, firstName, lastName) VALUES (?, ?, ?, ?, ?, ?)",
            user.getUsername(),
            user.getHashedPassword(),
            user.getSalt(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName()
        );
        try {
            user.register(username, hashedPassword, salt, email, firstName, lastName);
        } catch (Exception e) {
            if(e.getMessage().equals("Username already exists")) {
                return ResponseEntity.badRequest().body(Map.of("message", "User already exists"));
            }else {
                return ResponseEntity.badRequest().body(Map.of("message", "Error creating user"));
            }
        }
        session.setAttribute("username", username);
        return ResponseEntity.ok().body(Map.of("message", "User registered successfully"));
    }

    @PostMapping("/logout")//logout
    public ResponseEntity<Map<String,String>> checkSession(HttpServletRequest request, HttpSession session) {
        if(security.isSessionValid(session, request)) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not logged in"));
        }

        String username = (String) session.getAttribute("username");
        return ResponseEntity.ok().body(Map.of("message", "User logged in successfully"));
    }

    @PostMapping("/edit")//edit profile
    public ResponseEntity<Map<String,String>> editProfile(@RequestBody Map<String,String> requestData,HttpServletRequest request, HttpSession session) {
        if(security.isSessionValid(session, request)) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not logged in"));
        }
        String username = (String) session.getAttribute("username");
        return ResponseEntity.ok().body(Map.of("message", "User profile updated successfully"));
    }

    @PostMapping("/delete")//delete account
    public ResponseEntity<Map<String,String>> deleteAccount(@RequestBody Map<String,String> requestData, HttpServletRequest request, HttpSession session) {
        if(security.isSessionValid(session, request) || requestData.get("password") == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not logged in or passord not provided"));
        }
        
        //Database querry to delete user account
        String password = requestData.get("password");
        String username = (String) session.getAttribute("username");

        //Check if the password is correct
        
        //Delete the user account

        return ResponseEntity.ok().body(Map.of("message", "User "+  username +" account deleted successfully"));
    }

    @GetMapping("/info")//get user info
    public ResponseEntity<Map<String,String>> getUserInfo(HttpServletRequest request, HttpSession session) {
        if(security.isSessionValid(session, request)) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not logged in"));
        }
        String username = (String) session.getAttribute("username");
        return ResponseEntity.ok().body(Map.of("message", "User info retrieved successfully"));
    }

    @PostMapping("/upload-photo")//Upload profile photo
    public ResponseEntity<Map<String,String>> uploadProfilePhoto(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpSession session) {
        // Check if the user is logged in
        if(security.isSessionValid(session, request)) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not logged in"));
        }

        // if it yes, get the username from the session and create a new User object
        String username = (String) session.getAttribute("username");

        // Check if the file is empty
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "File is empty"));
        }
        // Check if the file is an image
        String contentType = file.getContentType();
        if (!contentType.startsWith("image/")) {
            return ResponseEntity.badRequest().body(Map.of("message", "File is not an image"));
        }
        // Check if the file size is less than 2MB
        if (file.getSize() > 2 * 1024 * 1024) {
            return ResponseEntity.badRequest().body(Map.of("message", "File size is too large"));
        }
        // Create a new file name and upload directory
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path uploadDir = Paths.get("uploads/photos");

        // Create a file object and set the file properties
        File profilePhoto = new File();
        profilePhoto.setName(fileName);
        profilePhoto.setPath(uploadDir.toString());
        profilePhoto.setType(contentType);
        profilePhoto.setSize(file.getSize());

        // Save the file to the server
        try {
            //save the file to the dir and database
            file.transferTo(uploadDir.resolve(fileName));
            profilePhoto.UploadToDatabase();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error uploading file"));
        }
        return ResponseEntity.ok().body(Map.of("message", "User profile photo uploaded successfully"));
    }
    
    @GetMapping("/profile-photo")//Get profile photo
    public ResponseEntity<Resource> getProfilePhoto(HttpServletRequest request, HttpSession session) throws MalformedURLException, IOException {
        // Check if the user is logged in
        if(security.isSessionValid(session, request)) {
            return ResponseEntity.badRequest().build();
        }
        // Get the username from the session and create a new User object
        String username = (String) session.getAttribute("username");
        // Get the user from the database
        File profilePhoto = new File();

        // Check if the profile photo exists
        Path filePath;
        filePath = Paths.get("uploads/photos").resolve(profilePhoto.getName());
        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }
        Resource fileResource = new UrlResource(filePath.toUri());
        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(fileResource);
    }









    /*
    @GetMapping("/download-file/{filename}")//Download file
    public ResponseEntity<Resource> downloadPhoto(@PathVariable String filename) throws IOException {
        Path filePath = Paths.get("uploads/pdf").resolve(filename);
        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        Resource fileResource = new UrlResource(filePath.toUri());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileResource.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileResource);
    }
    */
}
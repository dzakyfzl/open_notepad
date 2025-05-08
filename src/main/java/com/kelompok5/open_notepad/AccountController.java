package com.kelompok5.open_notepad;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;






@RestController
@RequestMapping("/api/account")
public class AccountController extends Security{

    //Authentication API
    @PostMapping("/signin")//login
    public ResponseEntity<Map<String,String>> signIn(@RequestBody Map<String,String> requestData,HttpServletRequest request, HttpSession session) {
        String username = requestData.get("username");
        String password = requestData.get("password");
        String IPString = request.getRemoteAddr();
        String sessionID = session.getId();
        session.setMaxInactiveInterval(60*60*24); // 1 day
        if(username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Username and password are required"));
        }
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
        if(username == null || password == null || email == null || firstName == null || lastName == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "All fields are required"));
        }
        //Hash password and salt it
        String salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);
        session.setAttribute("username", username);
        return ResponseEntity.ok().body(Map.of("message", "User registered successfully"));
    }

    @PostMapping("/logout")//logout
    public ResponseEntity<Map<String,String>> checkSession(HttpServletRequest request, HttpSession session) {
        if(isSessionValid(session, request)) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not logged in"));
        }
        return ResponseEntity.ok().body(Map.of("message", "User logged in successfully"));
    }

    @PostMapping("/edit")//edit profile
    public ResponseEntity<Map<String,String>> editProfile(@RequestBody Map<String,String> requestData,HttpServletRequest request, HttpSession session) {
        if(isSessionValid(session, request)) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not logged in"));
        }
        return ResponseEntity.ok().body(Map.of("message", "User profile updated successfully"));
    }

    @PostMapping("/delete")//delete account
    public ResponseEntity<Map<String,String>> deleteAccount(@RequestBody Map<String,String> requestData, HttpServletRequest request, HttpSession session) {
        if(isSessionValid(session, request) || requestData.get("password") == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not logged in or passord not provided"));
        }
        
        //Database querry to delete user account
        String password = requestData.get("password");
        String username = (String) session.getAttribute("username");
        return ResponseEntity.ok().body(Map.of("message", "User "+  username +" account deleted successfully"));
    }

    @GetMapping("/info")//get user info
    public ResponseEntity<Map<String,String>> getUserInfo(HttpServletRequest request, HttpSession session) {
        if(isSessionValid(session, request)) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not logged in"));
        }
        String username = (String) session.getAttribute("username");
        //Database querry to get user info
        String email = "";
        String firstName = "";
        String lastName = "";
        return ResponseEntity.ok().body(Map.of( "username", username,"email",email,"firstName",firstName,"lastName",lastName));
    }

}
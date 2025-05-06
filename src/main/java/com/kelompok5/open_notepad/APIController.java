package com.kelompok5.open_notepad;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;






@RestController
@RequestMapping("/api")
public class APIController {


    @PostMapping("/auth/signin")//login
    public ResponseEntity<Map<String,String>> signin(@RequestBody Map<String,String> requestData,HttpServletRequest request, HttpSession session) {
        String username = requestData.get("username");
        String password = requestData.get("password");
        String IPString = request.getRemoteAddr();
        String sessionID = session.getId();
        session.setMaxInactiveInterval(100);
        if(username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Username and password are required"));
        }
        //debugging
        session.setAttribute("username", username);

        return ResponseEntity.ok().body(Map.of("message", "User logged in successfully"));
    }

    @PostMapping("/auth/signup")//register
    public ResponseEntity<Map<String,String>> signUp(@RequestBody Map<String,String> requestData,HttpServletRequest request, HttpSession session) {
        String username = requestData.get("username");
        String password = requestData.get("password");
        String email = requestData.get("email");
        String firstName = requestData.get("firstName");
        String lastName = requestData.get("lastName");
        if(username == null || password == null || email == null || firstName == null || lastName == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "All fields are required"));
        }

        //debugging
        session.setAttribute("username", username);
        return ResponseEntity.ok().body(Map.of("message", "User registered successfully"));
    }
}

package com.kelompok5.open_notepad;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class PageController extends Security {
    @GetMapping("/")
    public String home(HttpSession session, HttpServletRequest request) {
        // Check if the user is logged in
        if (isSessionValid(session, request)) {
            // If not logged in, redirect to the main page
            return "redirect:/login";
        }
        return "index";
    }
    @GetMapping("/login")
    public String login(HttpSession session, HttpServletRequest request) {
        // Check if the user is logged in
        if (isSessionValid(session, request)) {
            // If logged in, redirect to the main page
            return "redirect:/";
        }
        return "authPage";
    }


    
}

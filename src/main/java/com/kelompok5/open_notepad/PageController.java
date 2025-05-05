package com.kelompok5.open_notepad;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.WebSession;
import org.springframework.ui.Model;

@Controller
public class PageController{
    @GetMapping("/")
    public String home(WebSession session) {
        // Check if the user is logged in
        if (session.getAttribute("username") == null) {
            // If not logged in, redirect to the main page
            return "redirect:/login";
        }
        return "index";
    }
    @GetMapping("/login")
    public String login(WebSession session) {
        // Check if the user is logged in
        if (session.getAttribute("username") != null) {
            // If logged in, redirect to the main page
            return "redirect:/";
        }
        return "loginPage";
    }
    @GetMapping("/register")
    public String signup(WebSession session) {
        // Check if the user is logged in
        if (session.getAttribute("username") != null) {
            // If logged in, redirect to the main page
            return "redirect:/";
        }
        return "signUp";
    }


    
}

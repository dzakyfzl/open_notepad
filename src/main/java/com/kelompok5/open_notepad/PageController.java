package com.kelompok5.open_notepad;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class PageController{

    private final Security security;

    public PageController(Security security) {
        this.security = security;
    }

    @GetMapping("/")
    public String home(HttpSession session, HttpServletRequest request) {
        // Check if the user is logged in
        if (!security.isSessionValid(session, request)) {
            System.out.println("User is not logged in");
            // If not logged in, redirect to the main page
            return "redirect:/login";
        }
        return "index";
    }
    
    @GetMapping("/login")
    public String login(HttpSession session, HttpServletRequest request) {
        // Check if the user is logged in
        if (security.isSessionValid(session, request)) {
            // If logged in, redirect to the main page
            return "redirect:/";
        }
        return "authPage";
    }

    @GetMapping("/user/profile")
    public String profile(HttpSession session, HttpServletRequest request) {
        // Check if the user is logged in
        if (!security.isSessionValid(session, request)) {
            // If not logged in, redirect to the main page
            return "redirect:/login";
        }
        return "profile";
    }

    @GetMapping("/user/notes/upload")
    public String uploadNotes(HttpSession session, HttpServletRequest request) {
        // Check if the user is logged in
        if (!security.isSessionValid(session, request)) {
            // If not logged in, redirect to the main page
            return "redirect:/login";
        }
        return "upload";
    }

    @GetMapping("/user/notes/edit/{id}")
    public String editNotes(HttpSession session, HttpServletRequest request, @PathVariable("id") String noteId, Model model) {
        // Check if the user is logged in
        if (!security.isSessionValid(session, request)) {
            // If not logged in, redirect to the main page
            return "redirect:/login";
        }
        System.out.println("Note ID: " + noteId);
        model.addAttribute("noteID", noteId);
        return "editNote";
    }

    @GetMapping("/user/notes")
    public String myNotes(HttpSession session, HttpServletRequest request) {
        // Check if the user is logged in
        if (!security.isSessionValid(session, request)) {
            // If not logged in, redirect to the main page
            return "redirect:/login";
        }
        return "myNote";
    }

    @GetMapping("/user/notes/saved")
    public String savedNotes(HttpSession session, HttpServletRequest request) {
        // Check if the user is logged in
        if (!security.isSessionValid(session, request)) {
            // If not logged in, redirect to the main page
            return "redirect:/login";
        }
        return "saved";
    }

    @GetMapping("/user/profile/edit")
    public String editProfile(HttpSession session, HttpServletRequest request) {
        // Check if the user is logged in
        if (!security.isSessionValid(session, request)) {
            // If not logged in, redirect to the main page
            return "redirect:/login";
        }
        return "editProfile";
    }

    @GetMapping("/note/view/{id}")
    public String notesPage(HttpSession session, HttpServletRequest request, @PathVariable("id") String noteId, Model model) {
        // Check if the user is logged in
        if (!security.isSessionValid(session, request)) {
            // If not logged in, redirect to the main page
            return "redirect:/login";
        }
        model.addAttribute("noteID", noteId);
        // Add any addition
        return "note";
    }

    @GetMapping("/admin")
    public String adminPage(HttpSession session, HttpServletRequest request) {
        // Check if the user is logged in
        if (!security.isSessionValid(session, request)) {
            // If not logged in, redirect to the main page
            return "redirect:/login";
        }
        if (!security.isAdmin(session)) {
            // If the user is not an admin, redirect to the main page
            return "redirect:/";
        }
        return "admin";
    }

    @GetMapping("/admin/profile")
    public String adminProfile(HttpSession session, HttpServletRequest request) {
        // Check if the user is logged in
        if (!security.isSessionValid(session, request)) {
            // If not logged in, redirect to the main page
            return "redirect:/login";
        }
        if (!security.isAdmin(session)) {
            // If the user is not an admin, redirect to the main page
            return "redirect:/";
        }
        return "adminProfile";
    }

    @GetMapping("/admin/profile/edit")
    public String adminEditProfile(HttpSession session, HttpServletRequest request) {
        // Check if the user is logged in
        if (!security.isSessionValid(session, request)) {
            // If not logged in, redirect to the main page
            return "redirect:/login";
        }
        if (!security.isAdmin(session)) {
            // If the user is not an admin, redirect to the main page
            return "redirect:/";
        }
        return "editAdminProfile";
    }
    
}

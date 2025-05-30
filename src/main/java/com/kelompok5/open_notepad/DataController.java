package com.kelompok5.open_notepad;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kelompok5.open_notepad.DAO.NoteDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/data")

public class DataController {
    @Autowired
    private NoteDAO noteDAO;

    @Autowired
    private Security security;


    @Autowired
    private JdbcTemplate jdbcTemplate;
    // Example method to save a user

    @GetMapping("/getAllnotes")
    public ResponseEntity<List<Map<String, Object>>> getAllnotes(HttpSession session, HttpServletRequest request) {
        if (!security.isSessionValid(session, request)) {
            return ResponseEntity.badRequest().build();
        }
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return ResponseEntity.badRequest().build();
        }
        List<Map<String, Object>> notes;
        try {
            notes = noteDAO.getAllnotes();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(List.of(Map.of("error", "Failed to retrieve notes")));
        }
        // Return note Name, Rating, Major, Course
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/filterNotes")
    public ResponseEntity<List<Map<String, Object>>> filterNotes(
            @RequestParam(defaultValue = "false") boolean IF,
            @RequestParam(defaultValue = "false") boolean DS,
            @RequestParam(defaultValue = "false") boolean RPL,
            @RequestParam(defaultValue = "false") boolean IT,
            @RequestParam(required = false) String course,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order,
            HttpSession session, HttpServletRequest request) {
        if (!security.isSessionValid(session, request)) {
            return ResponseEntity.badRequest().build();
        }
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            // Panggil metode filterNotes di NoteDAO dengan parameter yang diterima
            List<Map<String, Object>> notes = noteDAO.filterNotes(course, sortBy, order, IF, DS, RPL, IT);
            return ResponseEntity.ok(notes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(List.of(Map.of("error", "Failed to filter notes")));
        }
    }

    @GetMapping("/searchByNames")
    public ResponseEntity<List<Map<String, Object>>> searchByNames(@RequestParam String name,HttpSession session, HttpServletRequest request) {
        if (!security.isSessionValid(session, request)) {
            return ResponseEntity.badRequest().build();
        }
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            // Gunakan metode searchInCachedNotes dari NoteDAO
            List<Map<String, Object>> notes = noteDAO.searchInCachedNotes(name);
            return ResponseEntity.ok(notes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(List.of(Map.of("error", "Failed to search notes by name")));
        }
    }

    @GetMapping("/getMyNotes")
    public ResponseEntity<List<Map<String, Object>>> getMynotes(HttpSession session, HttpServletRequest request) {
        if (!security.isSessionValid(session, request)) {
           return ResponseEntity.badRequest().build();
        }
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return ResponseEntity.badRequest().build();
        }
        List<Map<String, Object>> notes;
        try {
            notes = noteDAO.getMynotes(username);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(List.of(Map.of("error", "Failed to retrieve notes")));
        }
        // Return note Name, Rating, Major, Course
        return ResponseEntity.ok(notes);
    }
}

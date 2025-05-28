package com.kelompok5.open_notepad;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kelompok5.open_notepad.DAO.NoteDAO;

@RestController
@RequestMapping("/api/data")

public class DataController {
    @Autowired
    private NoteDAO noteDAO;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    // Example method to save a user

    @GetMapping("/getAllnotes")
    public ResponseEntity<List<Map<String,Object>>> getAllnotes() {
        List<Map<String,Object>> notes;
        try{
            notes = noteDAO.getAllnotes();
        }catch (Exception e){
            return ResponseEntity.badRequest().body(List.of(Map.of("error", "Failed to retrieve notes")));
        }
        //Return note Name, Rating, Major, Course
        return ResponseEntity.ok(notes);
    }
}

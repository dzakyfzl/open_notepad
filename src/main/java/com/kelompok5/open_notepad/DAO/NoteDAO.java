package com.kelompok5.open_notepad.DAO;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.kelompok5.open_notepad.entity.Note;

@Component
public class NoteDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public List<Map<String, Object>> getAllnotes() {
        String sql = "SELECT * FROM Notes WHERE visibility = 1";
        // Querry to get all notes
        List<Map<String, Object>> notes = jdbcTemplate.queryForList(sql);
        return notes;
    }

    public List<Map<String, Object>> searchByNames(String noteName) {
        String sql = "SELECT * FROM Notes WHERE noteName LIKE ?";
        // Querry to get some notes
        List<Map<String, Object>> notes = jdbcTemplate.queryForList(sql, "%" + noteName + "%");
        return notes;
    }

    public void uploadToDatabase(Note note) {
        String sql = "INSERT INTO Notes (noteName, description, course, major, visibility, dateUploaded, username, fileID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        // Querry to insert note
        try {
            jdbcTemplate.update(sql, note.getTitle(), note.getDescription(), note.getCourse(), note.getMajor(), note.isVisibility(), note.getUploadDate(), note.getOwnerID(), note.getFile().getFileID());
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload note data to the database");
        }
        //upload note to database logic
    }
}
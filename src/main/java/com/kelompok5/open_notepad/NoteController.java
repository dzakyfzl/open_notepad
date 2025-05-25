package com.kelompok5.open_notepad;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kelompok5.open_notepad.DAO.NoteDAO;
import com.kelompok5.open_notepad.entity.Note;  

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteDAO noteDAO;

    public NoteController(NoteDAO noteDAO) {
        this.noteDAO = noteDAO;
    }

    //upload note to database
    @RequestMapping("/upload")
    public String uploadNote(Note note) {
        try {
            noteDAO.uploadToDatabase(note);
            return "Note uploaded successfully";
        } catch (Exception e) {
            return "Failed to upload note: " + e.getMessage();
        }
    }
}


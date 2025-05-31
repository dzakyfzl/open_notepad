package com.kelompok5.open_notepad;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kelompok5.open_notepad.DAO.BookmarkDAO;
import com.kelompok5.open_notepad.DAO.NoteDAO;
import com.kelompok5.open_notepad.DAO.RateDAO;
import com.kelompok5.open_notepad.DAO.ViewDAO;
import com.kelompok5.open_notepad.entity.Note;
import com.kelompok5.open_notepad.entity.Rate;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/interface")
public class DataInterfaceController {
    @Autowired
    private NoteDAO noteDAO;

    @Autowired
    private BookmarkDAO bookmarkDAO;

    @Autowired
    private RateDAO rateDAO;

    @Autowired
    private ViewDAO viewDAO;

    @Autowired
    private Security security;

    @GetMapping("/download")//Download file
    public ResponseEntity<Resource> download(@RequestParam("noteID") int noteID, HttpSession session, HttpServletRequest request)
        throws IOException {
        if(!security.isSessionValid(session, request)){
            return ResponseEntity.badRequest().build();
        }
        Note note = noteDAO.getFromDatabase(noteID);
        if (note == null) {
            return ResponseEntity.notFound().build();
        }
        Path filePath = Paths.get("uploads/pdf").resolve(note.getFile().getName());
        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        Resource fileResource = new UrlResource(filePath.toUri());
        return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
        fileResource.getFilename() + "\"")
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(fileResource);
    }

    @PostMapping("/bookmark")
    public ResponseEntity<Map<String, Object>> bookmark(
        @RequestParam("noteID") int noteID, HttpSession session, HttpServletRequest request){
        if(!security.isSessionValid(session, request)){
            return ResponseEntity.badRequest().body(Map.of("message", "user not logged in"));
        }
        String username = (String) session.getAttribute("username");
        
        try{
            bookmarkDAO.uplaodToDatabase(username, noteID);
        }catch (Exception e){
            System.out.println("error uploading bookmark to database :"+ e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", "error uploading bookmark to database"));
        }
        return ResponseEntity.ok(Map.of("message", "bookmark uploaded"));

    }

    @PostMapping("/view")
    public ResponseEntity<Map<String, Object>> view(
        @RequestParam("noteID") int noteID, HttpSession session, HttpServletRequest request){
        if(!security.isSessionValid(session, request)){
            return ResponseEntity.badRequest().body(Map.of("message", "user not logged in"));
        }
        String username = (String) session.getAttribute("username");
        
        try{
            viewDAO.uplaodToDatabase(username, noteID);
        }catch (Exception e){
            System.out.println("error uploading view to database :"+ e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", "error uploading view to database"));
        }
        return ResponseEntity.ok(Map.of("message", "view uploaded"));

    }

    @PostMapping("/rate")
    public ResponseEntity<Map<String, Object>> rate(
        @RequestParam("noteID") int noteID, @RequestParam("rate") int rate, HttpSession session, HttpServletRequest request){
        if(!security.isSessionValid(session, request)){
            return ResponseEntity.badRequest().body(Map.of("message", "user not logged in"));
        }
        String username = (String) session.getAttribute("username");
        if(rate < 0 || rate > 5){
            return ResponseEntity.badRequest().body(Map.of("message", "rate must be between 0 and 5"));
        }
        Rate rating = new Rate();
        rating.setModuleID(noteID);
        rating.setUserID(username);
        rating.setRating(rate);
        rating.setDateRated(new Date(System.currentTimeMillis()));

        
        
        try{
            rateDAO.uplaodToDatabase(rating);
        }catch (Exception e){
            System.out.println("error uploading view to database :"+ e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", "error uploading view to database"));
        }
        return ResponseEntity.ok(Map.of("message", "rating uploaded"));

    }
}

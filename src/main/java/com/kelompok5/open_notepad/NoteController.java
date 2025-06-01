package com.kelompok5.open_notepad;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kelompok5.open_notepad.DAO.BookmarkDAO;
import com.kelompok5.open_notepad.DAO.FileDAO;
import com.kelompok5.open_notepad.DAO.NoteDAO;
import com.kelompok5.open_notepad.DAO.RateDAO;
import com.kelompok5.open_notepad.DAO.UserDAO;
import com.kelompok5.open_notepad.DAO.ViewDAO;
import com.kelompok5.open_notepad.entity.File;
import com.kelompok5.open_notepad.entity.Note;
import com.kelompok5.open_notepad.entity.Rate;
import com.kelompok5.open_notepad.entity.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/note")
public class NoteController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Security security;

    @Autowired
    private FileDAO fileDAO;

    @Autowired
    private NoteDAO noteDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RateDAO rateDAO;

    @Autowired
    private ViewDAO viewDAO;

    @Autowired
    private BookmarkDAO bookmarkDAO;






    //upload note to database
    @PostMapping("/upload")
    public ResponseEntity<Map<String,String>> uploadNote(
        @RequestParam("file") MultipartFile file,
        @RequestParam("name") String name,
        @RequestParam("description") String description,
        @RequestParam("course") String course,
        @RequestParam("major") String major,
        @RequestParam("visibility") boolean visibility,
            HttpServletRequest request, HttpSession session) {

        
        // Check if the user is logged in
        if (!security.isSessionValid(session, request)) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not logged in"));
        }

         String username = (String) session.getAttribute("username");
        System.out.println("Username: " + username);
        try {
            // Check if the request data contains all required fields
            if (name.isEmpty() || description.isEmpty() || course.isEmpty() || major.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Missing required fields"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid request data"));
        }
        // Check if the user exists in the database
        User user = userDAO.getFromDatabase(username);
        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not found"));
        }

        // Check if the file is empty
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "File is empty"));
        }
        // Check if the file is an image
        String contentType = file.getContentType();
        if (!contentType.startsWith("application/pdf")) {   
            return ResponseEntity.badRequest().body(Map.of("message", "File is not an pdf file"));
        }
        // Check if the file size is less than 10MB
        if (file.getSize() > 10 * 1024 * 1024) {
            return ResponseEntity.badRequest().body(Map.of("message", "File size is too large"));
        }
        // Create a new file name and upload directory
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path uploadDir = Paths.get("uploads/pdf");

        // Create a file object and set the file properties
        File noteFile = new File(-1, fileName, uploadDir.toString(), file.getSize(), contentType);
        noteFile.setName(fileName);
        noteFile.setPath("uploads/pdf/" + fileName);
        noteFile.setType(contentType);
        noteFile.setSize(file.getSize());

        // Save the file to the server
        try {
            Files.createDirectories(uploadDir);
            file.transferTo(uploadDir.resolve(fileName));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error uploading file"));
        }

        // Save the file information to the database
        try {
            fileDAO.UploadToDatabase(noteFile);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }

        // Getting just FileID in database
        int ID = fileDAO.getFileID(fileName);
        noteFile.setFileID(ID);

        // Create a new note object
        Note note = new Note();
        note.setTitle(name);
        note.setDescription(description);
        note.setCourse(course);
        note.setMajor(major);
        note.setVisibility(visibility);
        note.setUploadDate(new java.sql.Date(System.currentTimeMillis()));
        note.setOwnerID(user.getUsername());
        note.setFile(noteFile);
        System.out.println("Note owner: " + note.getOwnerID());

        // Check if the note has a file associated with it
        try {
            // Save the note to the database
            noteDAO.uploadToDatabase(note);
        } catch (Exception e) {
            // Return an error response if the upload fails
            return ResponseEntity.badRequest().body(Map.of("message", "Error uploading note to database"));
        }

        return ResponseEntity.ok().body(Map.of("message", "Note uploaded successfully"));
    }

    // Method to delete a note
    @PostMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteNote(@RequestParam("noteID") int noteID, HttpServletRequest request, HttpSession session) {
        // Check if the user is logged in
        if (!security.isSessionValid(session, request)) {
            System.out.println("User is not logged in");
            return ResponseEntity.badRequest().body(Map.of("message", "User not logged in"));
        }
        String username = (String) session.getAttribute("username");

        User user = userDAO.getFromDatabase(username);
        if (user == null) {
            System.out.println("User not found");
            return ResponseEntity.badRequest().body(Map.of("message", "User not found"));
        }
        
        // Check if the note exists in the database
        Note note = noteDAO.getFromDatabase(noteID);
        System.out.println("Username: [" + username+"]");
        System.out.println("Note Username: [" + note.getOwnerID()+"]");
        if (note == null) {
            System.out.println("Note not found");
            return ResponseEntity.badRequest().body(Map.of("message", "Note not found"));
        }
        // Check if the note belongs to the user or the account ststus is admin
        if (!note.getOwnerID().trim().equalsIgnoreCase(username.trim()) && !security.isAdmin(session)) {
            System.out.println("You do not have permission to delete this note");
            return ResponseEntity.badRequest().body(Map.of("message", "You do not have permission to delete this note"));
        }
        //Create querry to get the file name associated with the note
        String sql = "SELECT fileID FROM Notes WHERE moduleID = ?";
        try {
            int fileID = jdbcTemplate.queryForObject(sql, Integer.class, noteID);
            // return the name of the file associated with the note
            File file = fileDAO.GetFromDatabase(fileID);
            String fileName = file.getName();
            // Delete the file from the server
            Path filePath = Paths.get("uploads/pdf", fileName);
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                return ResponseEntity.badRequest().body(Map.of("message", "Error deleting file from server"));
            }
        } catch (Exception e) {
            System.out.println("Error retrieving file ID for note: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", "Error retrieving file ID for note"));
        }
        //get file from database
        File file = fileDAO.GetFromDatabase(note.getFile().getFileID());
        if (file == null) {
            System.out.println("File not found");
            return ResponseEntity.badRequest().body(Map.of("message", "File not found"));
        }
        // Delete the file from the server
        Path filePath = Paths.get(file.getPath());
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.out.println("Error deleting file from server: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", "Error deleting file from server"));
        }        
        // Delete the note from the database
        try {
            bookmarkDAO.deleteFromNote(noteID);
            rateDAO.deleteFromNote(noteID);
            viewDAO.deleteFromNote(noteID);
            noteDAO.deleteFromDatabase(noteID);
        } catch (Exception e) {
            System.out.println("Error deleting note from database: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", "Error deleting note from database"));
        }
        // Delete the file from the database
        try {
            fileDAO.DeleteFromDatabase(file.getFileID());
        } catch (Exception e) {
            System.out.println("Error deleting file from database: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", "Error deleting file from database"));
        }
        return ResponseEntity.ok().body(Map.of("message", "Note deleted successfully"));

    }

    //method to get a note by ID
    @GetMapping("/get")
    public ResponseEntity<Map<String, Object>> getNoteByID(
        @RequestParam("noteID") int noteID,
        HttpServletRequest request,
        HttpSession session) {
    if (!security.isSessionValid(session, request)) {
        return ResponseEntity.badRequest().body(Map.of(
            "message", "User not logged in"
        ));
    }
    String username = (String) session.getAttribute("username");
    User user = userDAO.getFromDatabase(username);
    if (user == null) {
        return ResponseEntity.badRequest().body(Map.of(
            "message", "User not found"
        ));
    }
    Note note = noteDAO.getFromDatabase(noteID);
    if (note == null) {
        return ResponseEntity.badRequest().body(Map.of(
            "message", "Note not found"
        ));
    }
    Rate userRate = rateDAO.getRate(username, noteID);
    if (userRate == null) {
        userRate = new Rate();
        userRate.setRating(0);
    }
    float rate = rateDAO.getFromNote(noteID);

    Map<String, Object> response = new HashMap<>();
    response.put("name", note.getTitle());
    response.put("description", note.getDescription());
    response.put("course", note.getCourse());
    response.put("major", note.getMajor());
    response.put("visibility", note.isVisibility());
    response.put("uploadDate", note.getUploadDate());
    response.put("username", note.getOwnerID());
    response.put("userRate", userRate.getRating());
    response.put("rating", rate);
    response.put("views", viewDAO.getFromNote(noteID));
    response.put("bookmarked", bookmarkDAO.get(username, noteID));
    return ResponseEntity.ok().body(response);
}

    @GetMapping("/getfile")
    public ResponseEntity<Resource> getFile(@RequestParam("noteID") int noteID, HttpServletRequest request, HttpSession session) throws IOException{
        //check if user already log in
        if(!security.isSessionValid(session, request)){
            System.out.println("User is not logged in");
            return ResponseEntity.badRequest().body(null);
        }

        //get note from database
        Note note = noteDAO.getFromDatabase(noteID);
        if (note == null) {
            System.out.println("failed to get notes");
            return ResponseEntity.badRequest().body(null);
        }

        // Get file from server
        File noteFile =note.getFile();
        Path filePath;
        String contentType;
        filePath = Paths.get("uploads/pdf").resolve(noteFile.getName());
        if (!Files.exists(filePath)) {
            System.out.println("File not found");
            return ResponseEntity.notFound().build();
        }
        Resource fileResource = new UrlResource(filePath.toUri());
        contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        System.out.println("File type: " + contentType);

        
        return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(contentType))
        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + noteFile.getName() + "\"")
        .body(fileResource);
    }

    //update note to database
    @PostMapping("/update")
    public ResponseEntity<Map<String, String>> updateNote(
        @RequestParam("noteID") int noteID,
        @RequestParam("name") String name,
        @RequestParam("description") String description,
        @RequestParam("course") String course,
        @RequestParam("major") String major,
        @RequestParam("visibility") boolean visibility,
        HttpServletRequest request, HttpSession session) {

        // Check if the user is logged in
        if (!security.isSessionValid(session, request)) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not logged in"));
        }

        String username = (String) session.getAttribute("username");
        User user = userDAO.getFromDatabase(username);
        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not found"));
        }

        // Check if the note exists in the database
        Note note = noteDAO.getFromDatabase(noteID);
        if (note == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Note not found"));
        }

        // Check if the note belongs to the user
        if (!note.getOwnerID().equals(username)) {
            return ResponseEntity.badRequest().body(Map.of("message", "You do not have permission to update this note"));
        }

        // Update the note properties
        note.setTitle(name);
        note.setDescription(description);
        note.setCourse(course);
        note.setMajor(major);
        note.setVisibility(visibility);

        // Save the updated note to the database
        try {
            noteDAO.updateToDatabase(note);
            return ResponseEntity.ok().body(Map.of("message", "Note updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error updating note: " + e.getMessage()));
        }
    }

    //method to get file name by noteID
    @GetMapping("/getFileName")
    public ResponseEntity<Map<String, String>> getFileNameByNoteID(
        @RequestParam("noteID") int noteID,
        HttpServletRequest request, HttpSession session) {

        // Check if the user is logged in
        if (!security.isSessionValid(session, request)) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not logged in"));
        }

        String username = (String) session.getAttribute("username");
        User user = userDAO.getFromDatabase(username);
        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not found"));
        }

        // Get the note from the database
        Note note = noteDAO.getFromDatabase(noteID);
        if (note == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Note not found"));
        }

        // Get the file name from the note
        String fileName = note.getFile().getName();
        return ResponseEntity.ok().body(Map.of("fileName", fileName));
    }   

    @PostMapping("/visibility")
    public ResponseEntity<Map<String, String>> setVisibility(HttpSession session, HttpServletRequest request, @RequestParam("noteID") int noteID, @RequestParam("visibility") boolean visibility){
        if(!security.isSessionValid(session, request)){
            return ResponseEntity.badRequest().body(Map.of("message", "User not logged in"));
        }
        String username = (String) session.getAttribute("username");
        Note note;
        try {
            note = noteDAO.getFromDatabase(noteID);
        } catch (Exception e) {
            System.out.println("error retrieving note from database :"+ e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", "error retrieving note from database"));
        }
        //CHECK IF USER HAVE PERMISSION TO EDIT THE NOTE
        if(!note.getOwnerID().trim().equals(username.trim()) && !security.isAdmin(session)){
            return ResponseEntity.badRequest().body(Map.of("message", "You do not have permission to edit this note"));
        }
        note.setVisibility(visibility);
        try {
            noteDAO.updateToDatabase(note);
        } catch (Exception e) {
            System.out.println("error updating note to database :"+ e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", "error updating note from database"));
        }
        
        return ResponseEntity.ok().body(Map.of("message", "Note visibility updated successfully"));
    }
}


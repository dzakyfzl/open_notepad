package com.kelompok5.open_notepad.DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.kelompok5.open_notepad.entity.File;
import com.kelompok5.open_notepad.entity.Note;

@Component
public class NoteDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getAllnotes() {
        String sql = "SELECT n.name, COALESCE(AVG(r.rating), 0) AS avg_rating, n.major, n.course " +
                "FROM Notes n " +
                "LEFT JOIN Ratings r ON n.moduleID = r.moduleID " +
                "WHERE n.visibility = 1 " +
                "GROUP BY n.name, n.major, n.course";
        // Querry to get all notes
        List<Map<String, Object>> notes = jdbcTemplate.queryForList(sql);
        return notes;
    }

    public int searchNoteID(String noteName) {
        String sql = "SELECT moduleID FROM Notes WHERE noteName = ?";
        // Querry to get note ID by name
        try {
            return jdbcTemplate.queryForObject(sql, new Object[] { noteName }, Integer.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve note ID from the database");
        }
    }

    public List<Map<String, Object>> searchByNames(String noteName) {
        String sql = "SELECT * FROM Notes WHERE noteName LIKE ?";
        // Querry to get some notes
        List<Map<String, Object>> notes = jdbcTemplate.queryForList(sql, "%" + noteName + "%");
        return notes;
    }

    public void uploadToDatabase(Note note) {
        String sql = "INSERT INTO Notes (name, description, course, major, visibility, dateUploaded, username, fileID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        // Querry to insert note
        try {
            jdbcTemplate.update(sql, note.getTitle(), note.getDescription(), note.getCourse(), note.getMajor(),
                    note.isVisibility(), note.getUploadDate(), note.getOwnerID(), note.getFile().getFileID());
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload note data to the database");
        }
        // upload note to database logic
    }

    public Note getFromDatabase(int noteID) {
        String sql = "SELECT * FROM Files INNER JOIN Notes ON Files.fileID = Notes.fileID WHERE moduleID = ?";
        // Querry to get note by ID
        Note noted;
        System.out.println("Retrieving note with ID: " + noteID);
        try {
            noted = jdbcTemplate.queryForObject(sql, new Object[] { noteID }, (rs, rowNum) -> {
                Note note = new Note();
                note.setModuleID(rs.getInt("moduleID"));
                note.setOwnerID(rs.getString("username"));
                note.setTitle(rs.getString("name"));
                note.setDescription(rs.getString("description"));
                note.setCourse(rs.getString("course"));
                note.setMajor(rs.getString("major"));
                note.setUploadDate(rs.getDate("dateUploaded"));
                note.setVisibility(rs.getBoolean("visibility"));
                note.setFile(new File(
                        rs.getInt("fileID"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getLong("size"),
                        rs.getString("path")));
                return note;
            });
        } catch (Exception e) {
            System.out.println("Error retrieving note: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve note from the database");
        }
        return noted;
    }

    public void deleteFromDatabase(int noteID) {
        // Querry for deleting existing File
        String sqlFile = "SELECT fileID FROM Notes WHERE moduleID = ?";
        int fileID;
        try {
            fileID = jdbcTemplate.queryForObject(sqlFile, new Object[] { noteID }, Integer.class);
        } catch (Exception e) {
            System.out.println("Error deleting file associated with the note: " + e.getMessage());
            throw new RuntimeException("Failed to delete file associated with the note from the database");
        }

        String sql = "DELETE FROM Notes WHERE moduleID = ?";
        // Querry to delete note by ID
        try {
            jdbcTemplate.update(sql, noteID);
            jdbcTemplate.update("DELETE FROM Files WHERE fileID = ?", fileID);
        } catch (Exception e) {
            System.out.println("Error deleting note: " + e.getMessage());
            throw new RuntimeException("Failed to delete note from the database");
        }
    }

    public List<Map<String, Object>> filterNotes(String major, String course, String sortBy, String order) {
        StringBuilder sql = new StringBuilder(
                        "SELECT n.name, COALESCE(AVG(r.rating), 0) AS avg_rating, n.major, n.course " +
                        "FROM Notes n " +
                        "LEFT JOIN Ratings r ON n.moduleID = r.moduleID " +
                        "WHERE n.visibility = 1 ");

        List<Object> params = new ArrayList<>(); // List untuk parameter

        // Tambahkan filter hanya jika parameter diberikan
        if (major != null && !major.isEmpty()) {
            sql.append("AND n.major = ? ");
            params.add(major); // Tambahkan parameter major
        }

        if (course != null && !course.isEmpty()) {
            sql.append("AND n.course = ? ");
            params.add(course); // Tambahkan parameter course
        }

        sql.append("GROUP BY n.name, n.major, n.course "); // GROUP BY sebelum ORDER BY

        if (sortBy != null && !sortBy.isEmpty()) {
            sql.append("ORDER BY ").append(sortBy).append(" ");
            if (order != null && !order.isEmpty()) {
                sql.append(order);
            }
        }
        
        try {
            return jdbcTemplate.queryForList(sql.toString(), params.toArray());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to filter notes: " + e.getMessage());
        }
    }
}
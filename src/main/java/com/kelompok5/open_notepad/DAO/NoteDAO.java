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

    private List<Map<String, Object>> cachedNotes = null;
    private long lastUpdate = 0;
    private long timeExpired = 0; 
    private static final long CACHE_DURATION = 60000; 


    private boolean isCacheValid() {
        return cachedNotes != null && System.currentTimeMillis() < timeExpired;
    }

    private void updateCacheTimestamp() {
        lastUpdate = System.currentTimeMillis();
        timeExpired = lastUpdate + CACHE_DURATION;
        System.out.println("Cache updated at: " + new java.util.Date(lastUpdate));
        System.out.println("Cache expires at: " + new java.util.Date(timeExpired));
    }

    private void invalidateCache() {
        cachedNotes = null;
        lastUpdate = 0;
        timeExpired = 0;
        System.out.println("Cache invalidated at: " + new java.util.Date(System.currentTimeMillis()));
    }

    public List<Map<String, Object>> getAllnotes() {
        if (isCacheValid()) {
            System.out.println("Using cached notes data");
            return cachedNotes; 
        }

        System.out.println("Fetching fresh notes data from database");

        String sql = "SELECT n.moduleID AS id, " +
                "       n.name AS name, " +
                "       n.course, " +
                "       n.major, " +
                "       n.dateUploaded, " +
                "       a.username AS username, " +
                "       COALESCE(AVG(r.rating), 0) AS rating, " +
                "       COALESCE(v.total_views, 0) AS views " +
                "FROM Notes n " +
                "LEFT JOIN Ratings r ON n.moduleID = r.moduleID " +
                "LEFT JOIN ( " +
                "    SELECT v.moduleID, COUNT(*) AS total_views " +
                "    FROM Views v " +
                "    GROUP BY v.moduleID " +
                ") v ON n.moduleID = v.moduleID " +
                "LEFT JOIN Accounts a ON n.username = a.username " +
                "WHERE n.visibility = 1 " +
                "GROUP BY n.moduleID, n.name, n.course, n.major, n.dateUploaded, a.username, v.total_views";

        try {
            cachedNotes = jdbcTemplate.queryForList(sql); 
            updateCacheTimestamp(); 
            System.out.println("Cached " + cachedNotes.size() + " notes");
        } catch (Exception e) {
            System.out.println("Failed query from database: " + e.getMessage());
            return null;
        }

        return cachedNotes;
    }

    public List<Map<String, Object>> getMynotes(String username) {
        String sql = "SELECT n.moduleID AS id, " +
                "       n.name AS name, " +
                "       n.course, " +
                "       n.major, " +
                "       a.username AS username, " +
                "       COALESCE(AVG(r.rating), 0) AS rating, " +
                "       COALESCE(v.total_views, 0) AS views " +
                "FROM Notes n " +
                "LEFT JOIN Ratings r ON n.moduleID = r.moduleID " +
                "LEFT JOIN ( " +
                "    SELECT v.moduleID, COUNT(*) AS total_views " +
                "    FROM Views v " +
                "    GROUP BY v.moduleID " +
                ") v ON n.moduleID = v.moduleID " +
                "LEFT JOIN Accounts a ON n.username = a.username " +
                "WHERE n.username = ? " +
                "GROUP BY n.moduleID, n.name, n.course, n.major, a.username, v.total_views";

        try {
            List<Map<String, Object>> response = jdbcTemplate.queryForList(sql, username);
            return response;
        } catch (Exception e) {
            System.out.println("Failed query from database: " + e.getMessage());
            return null;
        }
    }

    public List<Map<String, Object>> searchInCachedNotes(String noteName) {
        if (!isCacheValid()) {
            getAllnotes(); 
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> note : cachedNotes) {
            String name = (String) note.get("name");
            if (name != null && name.toLowerCase().contains(noteName.toLowerCase())) {
                result.add(note);
            }
        }
        System.out.println("Found " + result.size() + " notes matching '" + noteName + "' in cache");
        return result;
    }

    public int searchNoteID(String noteName) {
        String sql = "SELECT moduleID FROM Notes WHERE noteName = ?";
  
        try {
            return jdbcTemplate.queryForObject(sql, new Object[] { noteName }, Integer.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve note ID from the database");
        }
    }

    public void uploadToDatabase(Note note) {
        String sql = "INSERT INTO Notes (name, description, course, major, visibility, dateUploaded, username, fileID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try {
            jdbcTemplate.update(sql, note.getTitle(), note.getDescription(), note.getCourse(), note.getMajor(),
                    note.isVisibility(), note.getUploadDate(), note.getOwnerID(), note.getFile().getFileID());

            invalidateCache();
            System.out.println("Cache invalidated after adding new note");

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload note data to the database");
        }
    }

    // update note in database
    public void updateToDatabase(Note note) {
        String sql = "UPDATE Notes SET name = ?, description = ?, course = ?, major = ?, visibility = ? WHERE moduleID = ?";
        // Querry to update note
        try {
            int rowsAffected = jdbcTemplate.update(sql, note.getTitle(), note.getDescription(), note.getCourse(),
                    note.getMajor(),
                    note.isVisibility(), note.getModuleID());

            // 🗑️ Invalidate cache jika ada perubahan
            if (rowsAffected > 0) {
                invalidateCache();
                System.out.println("Cache invalidated after updating note");
            }

        } catch (Exception e) {
            System.out.println("Error updating note: " + e.getMessage());
            throw new RuntimeException("Failed to update note in the database");
        }
    }

    public Note getFromDatabase(int noteID) {
        String sql = "SELECT f.fileID, f.name, f.type, f.size, f.path, " +
                "       n.moduleID, n.username, n.name AS noteName, n.description, " +
                "       n.course, n.major, n.dateUploaded, n.visibility " +
                "FROM Files f INNER JOIN Notes n ON f.fileID = n.fileID WHERE moduleID = ?";
        // Querry to get note by ID
        Note noted;
        System.out.println("Retrieving note with ID: " + noteID);
        try {
            noted = jdbcTemplate.queryForObject(sql, new Object[] { noteID }, (rs, rowNum) -> {
                Note note = new Note();
                note.setFile(new File(
                        rs.getInt("fileID"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getLong("size"),
                        rs.getString("path")));
                note.setModuleID(rs.getInt("moduleID"));
                note.setOwnerID(rs.getString("username"));
                note.setTitle(rs.getString("noteName"));
                note.setDescription(rs.getString("description"));
                note.setCourse(rs.getString("course"));
                note.setMajor(rs.getString("major"));
                note.setUploadDate(rs.getDate("dateUploaded"));
                note.setVisibility(rs.getBoolean("visibility"));

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
            int rowsAffected = jdbcTemplate.update(sql, noteID);
            jdbcTemplate.update("DELETE FROM Files WHERE fileID = ?", fileID);

            if (rowsAffected > 0) {
                invalidateCache();
                System.out.println("Cache invalidated after deleting note");
            }

        } catch (Exception e) {
            System.out.println("Error deleting note: " + e.getMessage());
            throw new RuntimeException("Failed to delete note from the database");
        }
    }

    public List<Map<String, Object>> filterNotes(String course, String sortBy, String sortOrder, boolean IF, boolean DS,
            boolean RPL, boolean IT) {

        if (!isCacheValid()) {
            getAllnotes(); // Load fresh data jika cache expired
        }

        System.out.println("Filtering " + cachedNotes.size() + " notes from cache");

        // 🔍 Filter berdasarkan kriteria
        List<Map<String, Object>> filteredNotes = new ArrayList<>();

        for (Map<String, Object> note : cachedNotes) {
            // Filter berdasarkan major
            boolean majorMatch = matchesMajorFilter(note, IF, DS, RPL, IT);

            // Filter berdasarkan course
            boolean courseMatch = matchesCourseFilter(note, course);

            // Tambahkan ke hasil jika memenuhi semua filter
            if (majorMatch && courseMatch) {
                filteredNotes.add(note);
            }
        }

        // 📊 Sort berdasarkan kriteria
        if (sortBy != null && !sortBy.isEmpty()) {
            sortNotes(filteredNotes, sortBy, sortOrder);
        }

        System.out.println("Filter completed: " + filteredNotes.size() + " notes match criteria");
        return filteredNotes;
    }

    private boolean matchesMajorFilter(Map<String, Object> note, boolean IF, boolean DS, boolean RPL, boolean IT) {
        // Jika tidak ada filter major yang aktif, return true
        if (!IF && !DS && !RPL && !IT) {
            return true;
        }

        String major = (String) note.get("major");
        if (major == null) {
            return false;
        }

        return (IF && major.equals("S1 Informatika")) ||
                (DS && major.equals("S1 Data Sains")) ||
                (RPL && major.equals("S1 Rekayasa Perangkat Lunak")) ||
                (IT && major.equals("S1 Teknologi Informasi"));
    }

    private boolean matchesCourseFilter(Map<String, Object> note, String course) {
        if (course == null || course.equalsIgnoreCase("All")) {
            return true;
        }

        String noteCourse = (String) note.get("course");
        return noteCourse != null && noteCourse.equals(course);
    }

    private void sortNotes(List<Map<String, Object>> notes, String sortBy, String sortOrder) {
        notes.sort((a, b) -> {
            int result = compareNotes(a, b, sortBy);
            return sortOrder != null && sortOrder.equalsIgnoreCase("desc") ? -result : result;
        });
    }

    private int compareNotes(Map<String, Object> a, Map<String, Object> b, String sortBy) {
        switch (sortBy.toLowerCase()) {
            case "letter":
                return compareStrings((String) a.get("name"), (String) b.get("name"));

            case "rating":
                return compareNumbers(a.get("rating"), b.get("rating"));

            case "view":
                return compareNumbers(a.get("views"), b.get("views"));

            case "date":
                return compareDates(a.get("dateUploaded"), b.get("dateUploaded"));

            default:
                return compareStrings((String) a.get("name"), (String) b.get("name"));
        }
    }

    private int compareStrings(String a, String b) {
        if (a == null && b == null)
            return 0;
        if (a == null)
            return -1;
        if (b == null)
            return 1;
        return a.compareToIgnoreCase(b);
    }

    private int compareNumbers(Object a, Object b) {
        Double numA = a instanceof Number ? ((Number) a).doubleValue() : 0.0;
        Double numB = b instanceof Number ? ((Number) b).doubleValue() : 0.0;
        return numA.compareTo(numB);
    }

    private int compareDates(Object a, Object b) {
        if (a instanceof java.util.Date && b instanceof java.util.Date) {
            return ((java.util.Date) a).compareTo((java.util.Date) b);
        }
        // Fallback to string comparison if not dates
        return compareStrings(String.valueOf(a), String.valueOf(b));
    }

    public void forceRefreshCache() {
        invalidateCache();
        getAllnotes();
        System.out.println("Cache force refreshed");
    }

    public Map<String, Object> getCacheStatus() {
        Map<String, Object> status = new java.util.HashMap<>();
        status.put("isCached", cachedNotes != null);
        status.put("isValid", isCacheValid());
        status.put("lastUpdate", lastUpdate > 0 ? new java.util.Date(lastUpdate) : null);
        status.put("timeExpired", timeExpired > 0 ? new java.util.Date(timeExpired) : null);
        status.put("cacheSize", cachedNotes != null ? cachedNotes.size() : 0);
        status.put("timeRemaining", timeExpired > 0 ? Math.max(0, timeExpired - System.currentTimeMillis()) : 0);
        return status;
    }
}

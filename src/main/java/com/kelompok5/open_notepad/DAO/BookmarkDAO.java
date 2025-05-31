package com.kelompok5.open_notepad.DAO;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class BookmarkDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public BookmarkDAO(JdbcTemplate jdbcTemplate2) {
        // TODO Auto-generated constructor stub
    }

    public int getFromUser(String userID) {
        String sql = "SELECT COUNT(*) AS bookmark FROM Bookmarks WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[] { userID }, (rs, rowNum) -> rs.getInt("bookmark"));

        } catch (Exception e) {
            return -1;
        }
    }

    public int getFromNote(int moduleID) {
        String sql = "SELECT COUNT(*) AS bookmark FROM Bookmarks WHERE moduleID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[] { moduleID }, (rs, rowNum) -> rs.getInt("bookmark"));

        } catch (Exception e) {
            return -1;
        }

    }

    public boolean get(String userID, int moduleID) {
        String sql = "SELECT * FROM Bookmarks WHERE username = ? AND moduleID = ?";
        try {
            jdbcTemplate.queryForObject(sql, new Object[] { userID, moduleID }, (rs, rowNum) -> rs.getInt("moduleID"));
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    public void uplaodToDatabase(String username, int noteID) {
        String sql = "INSERT INTO Bookmarks(moduleID, username, dateBookmarked) VALUES (?,?,?)";
        Date timestamp = new Date(System.currentTimeMillis());
        jdbcTemplate.update(sql, noteID, username, timestamp);
    }

    public void deleteFromUser(String userID) {
        String sql = "DELETE FROM Bookmarks WHERE username = ?";
        jdbcTemplate.update(sql, userID);
    }

    public void deleteFromNote(int moduleID) {
        String sql = "DELETE FROM Bookmarks WHERE moduleID = ?";
        jdbcTemplate.update(sql, moduleID);
    }

    public void delete(int moduleID, String userID) {
        String sql = "DELETE FROM Bookmarks WHERE moduleID = ? AND username = ?";
        jdbcTemplate.update(sql, moduleID, userID);
    }

    public List<Map<String, Object>> getBookmarkedNotes(String username) {
        String sql = "SELECT n.moduleID AS id, " +
                "       n.name AS name, " +
                "       n.course, " +
                "       n.major, " +
                "       a.username AS username, " +
                "       COALESCE(AVG(r.rating), 0) AS rating, " +
                "       COALESCE(v.total_views, 0) AS views, " +
                "       b.dateBookmarked " +
                "FROM Notes n " +
                "INNER JOIN Bookmarks b ON n.moduleID = b.moduleID " +
                "LEFT JOIN Ratings r ON n.moduleID = r.moduleID " +
                "LEFT JOIN ( " +
                "    SELECT v.moduleID, COUNT(*) AS total_views " +
                "    FROM Views v " +
                "    GROUP BY v.moduleID " +
                ") v ON n.moduleID = v.moduleID " +
                "LEFT JOIN Accounts a ON n.username = a.username " +
                "WHERE n.visibility = 1 " +
                "AND b.username = ? " +
                "GROUP BY n.moduleID, n.name, n.course, n.major, a.username, v.total_views, b.dateBookmarked " +
                "ORDER BY b.dateBookmarked DESC";

        try {
            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, username);
            return result;
        } catch (EmptyResultDataAccessException e) {
            return List.of(); 
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}

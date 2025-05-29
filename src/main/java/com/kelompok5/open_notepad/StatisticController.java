package com.kelompok5.open_notepad;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kelompok5.open_notepad.DAO.RateDAO;
import com.kelompok5.open_notepad.DAO.ViewDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/statistics")
public class StatisticController {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Security security;

    @Autowired
    private RateDAO rateDAO;

    @Autowired
    private ViewDAO viewDAO;



    // ==================== VIEWS ENDPOINTS ====================
    
    /**
     * Get view for one note
     */
    @GetMapping("/views")
    public ResponseEntity<Map<String, Object>> getViewStatistics(
            @RequestParam("noteID") int noteID,
            HttpServletRequest request, HttpSession session) {
        
        // Check if user is logged in
        if (!security.isSessionValid(session, request)) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not logged in"));
        }
        int view = viewDAO.getFromNote(noteID);
        if (view == -1) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error retrieving views"));
        }
        return ResponseEntity.ok().body(Map.of("views", view));
    }


    // ==================== RATINGS ENDPOINTS ====================
    
    /**
     * Get ratings one note
     */
    @GetMapping("/ratings")
    public ResponseEntity<Map<String, Object>> getRatingStatistics(
            @RequestParam("noteID") int noteID,
            HttpServletRequest request, HttpSession session) {
        
        // Check if user is logged in
        if (!security.isSessionValid(session, request)) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not logged in"));
        }
        float rate = rateDAO.getFromNote(noteID);
        if (rate == -1) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error retrieving ratings"));
        }
        return ResponseEntity.ok().body(Map.of("ratings", rate));
        

    }


    // ==================== BOOKMARKS ENDPOINTS ====================
    
    /**
     * Get bookmark statistics
     */
    @GetMapping("/bookmarks")
    public ResponseEntity<Map<String, Object>> getBookmarkStatistics(
            @RequestParam("noteID") int noteID,
            HttpServletRequest request, HttpSession session) {
        // Check if user is logged in
        if (!security.isSessionValid(session, request)) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not logged in"));
        }
        float rate = rateDAO.getFromNote(noteID);
        if (rate == -1) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error retrieving ratings"));
        }
        return ResponseEntity.ok().body(Map.of("bookmarks", rate));
    }

    // ==================== DASHBOARD STATISTICS ====================
    
    /**
     * Get overall statistics dashboard
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardStatistics(
            HttpServletRequest request, HttpSession session) {
        
        // Check if user is logged in
        if (!security.isSessionValid(session, request)) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not logged in"));
        }

        try {
            // Get total views
            String totalViewsSql = "SELECT COUNT(*) FROM ViewStatistics";
            int totalViews = jdbcTemplate.queryForObject(totalViewsSql, Integer.class);
            
            // Get today's views
            String todayViewsSql = "SELECT COUNT(*) FROM ViewStatistics WHERE DATE(viewedAt) = CURDATE()";
            int todayViews = jdbcTemplate.queryForObject(todayViewsSql, Integer.class);
            
            // Get total ratings
            String totalRatingsSql = "SELECT COUNT(*) FROM RatingStatistics";
            int totalRatings = jdbcTemplate.queryForObject(totalRatingsSql, Integer.class);
            
            // Get average rating
            String avgRatingSql = "SELECT COALESCE(AVG(rating), 0) FROM RatingStatistics";
            double averageRating = jdbcTemplate.queryForObject(avgRatingSql, Double.class);
            
            // Get total bookmarks
            String totalBookmarksSql = "SELECT COUNT(*) FROM BookmarkStatistics";
            int totalBookmarks = jdbcTemplate.queryForObject(totalBookmarksSql, Integer.class);
            
            // Get today's bookmarks
            String todayBookmarksSql = "SELECT COUNT(*) FROM BookmarkStatistics WHERE DATE(bookmarkedAt) = CURDATE()";
            int todayBookmarks = jdbcTemplate.queryForObject(todayBookmarksSql, Integer.class);
            
            Map<String, Object> dashboard = Map.of(
                "views", Map.of(
                    "total", totalViews,
                    "today", todayViews
                ),
                "ratings", Map.of(
                    "total", totalRatings,
                    "average", Math.round(averageRating * 100.0) / 100.0
                ),
                "bookmarks", Map.of(
                    "total", totalBookmarks,
                    "today", todayBookmarks
                )
            );
            
            return ResponseEntity.ok().body(Map.of(
                "message", "Dashboard statistics retrieved successfully",
                "data", dashboard
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error retrieving dashboard statistics: " + e.getMessage()));
        }
    }
}
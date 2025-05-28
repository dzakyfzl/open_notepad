package com.opennotepad.controller;

import com.opennotepad.dto.StatisticResponse;
import com.opennotepad.dto.ViewStatisticRequest;
import com.opennotepad.dto.RatingStatisticRequest;
import com.opennotepad.dto.BookmarkStatisticRequest;
import com.opennotepad.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "*")
public class StatisticController {

    private final StatisticService statisticService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Security security;

    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    // ==================== VIEWS ENDPOINTS ====================
    
    /**
     * Get view statistics for all notes or specific note
     */
    @GetMapping("/views")
    public ResponseEntity<Map<String, Object>> getViewStatistics(
            @RequestParam(required = false) String moduleID,
            @RequestParam(required = false) String period,
            HttpServletRequest request, HttpSession session) {
        
        // Check if user is logged in
        if (!security.isSessionValid(session, request)) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not logged in"));
        }

        try {
            String sql;
            Object[] params;
            
            if (moduleID != null) {
                // Get views for specific note
                if (period != null) {
                    sql = "SELECT COUNT(*) as totalViews, COUNT(DISTINCT username) as uniqueViews " +
                          "FROM ViewStatistics WHERE moduleID = ? AND viewedAt >= " + getPeriodCondition(period);
                    params = new Object[]{Integer.parseInt(moduleID)};
                } else {
                    sql = "SELECT COUNT(*) as totalViews, COUNT(DISTINCT username) as uniqueViews " +
                          "FROM ViewStatistics WHERE moduleID = ?";
                    params = new Object[]{Integer.parseInt(moduleID)};
                }
                
                Map<String, Object> result = jdbcTemplate.queryForMap(sql, params);
                return ResponseEntity.ok().body(Map.of(
                    "message", "View statistics retrieved successfully",
                    "data", result
                ));
                
            } else {
                // Get top viewed notes
                if (period != null) {
                    sql = "SELECT n.moduleID, n.title, COUNT(v.moduleID) as viewCount " +
                          "FROM Notes n LEFT JOIN ViewStatistics v ON n.moduleID = v.moduleID " +
                          "WHERE v.viewedAt >= " + getPeriodCondition(period) + " OR v.viewedAt IS NULL " +
                          "GROUP BY n.moduleID, n.title ORDER BY viewCount DESC LIMIT 10";
                } else {
                    sql = "SELECT n.moduleID, n.title, COUNT(v.moduleID) as viewCount " +
                          "FROM Notes n LEFT JOIN ViewStatistics v ON n.moduleID = v.moduleID " +
                          "GROUP BY n.moduleID, n.title ORDER BY viewCount DESC LIMIT 10";
                }
                
                List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
                return ResponseEntity.ok().body(Map.of(
                    "message", "Top viewed notes retrieved successfully",
                    "data", results
                ));
            }
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error retrieving view statistics: " + e.getMessage()));
        }
    }

    /**
     * Record a new view for a note
     */
    @PostMapping("/views")
    public ResponseEntity<Map<String, String>> recordView(
            @RequestBody Map<String, String> requestData,
            HttpServletRequest request, HttpSession session) {
        
        // Check if user is logged in
        if (!security.isSessionValid(session, request)) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not logged in"));
        }

        String username = (String) session.getAttribute("username");
        String moduleIdStr = requestData.get("moduleID");
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = request.getRemoteAddr();

        if (moduleIdStr == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Module ID is required"));
        }

        try {
            int moduleID = Integer.parseInt(moduleIdStr);
            
            // Check if note exists
            String checkNoteSql = "SELECT COUNT(*) FROM Notes WHERE moduleID = ?";
            int noteCount = jdbcTemplate.queryForObject(checkNoteSql, Integer.class, moduleID);
            
            if (noteCount == 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Note not found"));
            }

            // Check if user has viewed this note in the last hour (to prevent spam)
            String checkRecentViewSql = "SELECT COUNT(*) FROM ViewStatistics WHERE moduleID = ? AND username = ? AND viewedAt > DATE_SUB(NOW(), INTERVAL 1 HOUR)";
            int recentViews = jdbcTemplate.queryForObject(checkRecentViewSql, Integer.class, moduleID, username);
            
            if (recentViews == 0) {
                // Record the view
                String insertViewSql = "INSERT INTO ViewStatistics (moduleID, username, userAgent, ipAddress, viewedAt) VALUES (?, ?, ?, ?, NOW())";
                jdbcTemplate.update(insertViewSql, moduleID, username, userAgent, ipAddress);
            }

            return ResponseEntity.ok().body(Map.of("message", "View recorded successfully"));
            
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid module ID format"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error recording view: " + e.getMessage()));
        }
    }

    /**
     * Get top viewed notes
     */
    @GetMapping("/views/top")
    public ResponseEntity<Map<String, Object>> getTopViewedNotes(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String period,
            HttpServletRequest request, HttpSession session) {
        
        // Check if user is logged in
        if (!security.isSessionValid(session, request)) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not logged in"));
        }

        try {
            String sql;
            Object[] params;
            
            if (period != null) {
                sql = "SELECT n.moduleID, n.title, COUNT(v.moduleID) as viewCount " +
                      "FROM Notes n LEFT JOIN ViewStatistics v ON n.moduleID = v.moduleID " +
                      "WHERE v.viewedAt >= " + getPeriodCondition(period) + " OR v.viewedAt IS NULL " +
                      "GROUP BY n.moduleID, n.title ORDER BY viewCount DESC LIMIT ?";
                params = new Object[]{limit};
            } else {
                sql = "SELECT n.moduleID, n.title, COUNT(v.moduleID) as viewCount " +
                      "FROM Notes n LEFT JOIN ViewStatistics v ON n.moduleID = v.moduleID " +
                      "GROUP BY n.moduleID, n.title ORDER BY viewCount DESC LIMIT ?";
                params = new Object[]{limit};
            }
            
            List<Map<String, Object>> topViewed = jdbcTemplate.queryForList(sql, params);

            return ResponseEntity.ok().body(Map.of(
                    "success", "true",
                    "message", "Top viewed notes retrieved successfully",
                    "data", topViewed
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", "false",
                    "message", "Failed to retrieve top viewed notes: " + e.getMessage()
            ));
        }
    }

    // ==================== RATINGS ENDPOINTS ====================
    
    /**
     * Get rating statistics
     */
    @GetMapping("/ratings")
    public ResponseEntity<Map<String, Object>> getRatingStatistics(
            @RequestParam(required = false) String moduleID,
            @RequestParam(required = false) String period,
            HttpServletRequest request, HttpSession session) {
        
        // Check if user is logged in
        if (!security.isSessionValid(session, request)) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not logged in"));
        }

        try {
            String sql;
            Object[] params;
            
            if (moduleID != null) {
                // Get ratings for specific note
                if (period != null) {
                    sql = "SELECT AVG(rating) as averageRating, COUNT(*) as totalRatings, " +
                          "SUM(CASE WHEN rating = 5 THEN 1 ELSE 0 END) as fiveStars, " +
                          "SUM(CASE WHEN rating = 4 THEN 1 ELSE 0 END) as fourStars, " +
                          "SUM(CASE WHEN rating = 3 THEN 1 ELSE 0 END) as threeStars, " +
                          "SUM(CASE WHEN rating = 2 THEN 1 ELSE 0 END) as twoStars, " +
                          "SUM(CASE WHEN rating = 1 THEN 1 ELSE 0 END) as oneStar " +
                          "FROM RatingStatistics WHERE moduleID = ? AND ratedAt >= " + getPeriodCondition(period);
                    params = new Object[]{Integer.parseInt(moduleID)};
                } else {
                    sql = "SELECT AVG(rating) as averageRating, COUNT(*) as totalRatings, " +
                          "SUM(CASE WHEN rating = 5 THEN 1 ELSE 0 END) as fiveStars, " +
                          "SUM(CASE WHEN rating = 4 THEN 1 ELSE 0 END) as fourStars, " +
                          "SUM(CASE WHEN rating = 3 THEN 1 ELSE 0 END) as threeStars, " +
                          "SUM(CASE WHEN rating = 2 THEN 1 ELSE 0 END) as twoStars, " +
                          "SUM(CASE WHEN rating = 1 THEN 1 ELSE 0 END) as oneStar " +
                          "FROM RatingStatistics WHERE moduleID = ?";
                    params = new Object[]{Integer.parseInt(moduleID)};
                }
                
                Map<String, Object> result = jdbcTemplate.queryForMap(sql, params);
                return ResponseEntity.ok().body(Map.of(
                    "message", "Rating statistics retrieved successfully",
                    "data", result
                ));
                
            } else {
                // Get top rated notes
                if (period != null) {
                    sql = "SELECT n.moduleID, n.title, AVG(r.rating) as averageRating, COUNT(r.rating) as totalRatings " +
                          "FROM Notes n LEFT JOIN RatingStatistics r ON n.moduleID = r.moduleID " +
                          "WHERE r.ratedAt >= " + getPeriodCondition(period) + " OR r.ratedAt IS NULL " +
                          "GROUP BY n.moduleID, n.title HAVING COUNT(r.rating) >= 3 " +
                          "ORDER BY averageRating DESC, totalRatings DESC LIMIT 10";
                } else {
                    sql = "SELECT n.moduleID, n.title, AVG(r.rating) as averageRating, COUNT(r.rating) as totalRatings " +
                          "FROM Notes n LEFT JOIN RatingStatistics r ON n.moduleID = r.moduleID " +
                          "GROUP BY n.moduleID, n.title HAVING COUNT(r.rating) >= 3 " +
                          "ORDER BY averageRating DESC, totalRatings DESC LIMIT 10";
                }
                
                List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
                return ResponseEntity.ok().body(Map.of(
                    "message", "Top rated notes retrieved successfully",
                    "data", results
                ));
            }
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error retrieving rating statistics: " + e.getMessage()));
        }
    }

    /**
     * Submit or update a rating
     */
    @PostMapping("/ratings")
    public ResponseEntity<Map<String, String>> submitRating(
            @RequestBody Map<String, String> requestData,
            HttpServletRequest request, HttpSession session) {
        
        // Check if user is logged in
        if (!security.isSessionValid(session, request)) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not logged in"));
        }

        String username = (String) session.getAttribute("username");
        String moduleIdStr = requestData.get("moduleID");
        String ratingStr = requestData.get("rating");
        String review = requestData.get("review");

        if (moduleIdStr == null || ratingStr == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Module ID and rating are required"));
        }

        try {
            int moduleID = Integer.parseInt(moduleIdStr);
            int rating = Integer.parseInt(ratingStr);
            
            if (rating < 1 || rating > 5) {
                return ResponseEntity.badRequest().body(Map.of("message", "Rating must be between 1 and 5"));
            }

            // Check if note exists
            String checkNoteSql = "SELECT COUNT(*) FROM Notes WHERE moduleID = ?";
            int noteCount = jdbcTemplate.queryForObject(checkNoteSql, Integer.class, moduleID);
            
            if (noteCount == 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Note not found"));
            }

            // Check if user has already rated this note
            String checkRatingSql = "SELECT COUNT(*) FROM RatingStatistics WHERE moduleID = ? AND username = ?";
            int existingRating = jdbcTemplate.queryForObject(checkRatingSql, Integer.class, moduleID, username);
            
            if (existingRating > 0) {
                // Update existing rating
                String updateSql = "UPDATE RatingStatistics SET rating = ?, review = ?, ratedAt = NOW() WHERE moduleID = ? AND username = ?";
                jdbcTemplate.update(updateSql, rating, review, moduleID, username);
            } else {
                // Insert new rating
                String insertSql = "INSERT INTO RatingStatistics (moduleID, username, rating, review, ratedAt) VALUES (?, ?, ?, ?, NOW())";
                jdbcTemplate.update(insertSql, moduleID, username, rating, review);
            }

            return ResponseEntity.ok().body(Map.of("message", "Rating submitted successfully"));
            
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid number format"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error submitting rating: " + e.getMessage()));
        }
    }

    /**
     * Get top rated notes
     */
    @GetMapping("/ratings/top")
    public ResponseEntity<Map<String, Object>> getTopRatedNotes(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String period,
            HttpServletRequest request, HttpSession session) {
        
        // Check if user is logged in
        if (!security.isSessionValid(session, request)) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not logged in"));
        }

        try {
            String sql;
            Object[] params;
            
            if (period != null) {
                sql = "SELECT n.moduleID, n.title, AVG(r.rating) as averageRating, COUNT(r.rating) as totalRatings " +
                      "FROM Notes n LEFT JOIN RatingStatistics r ON n.moduleID = r.moduleID " +
                      "WHERE r.ratedAt >= " + getPeriodCondition(period) + " OR r.ratedAt IS NULL " +
                      "GROUP BY n.moduleID, n.title HAVING COUNT(r.rating) >= 3 " +
                      "ORDER BY averageRating DESC, totalRatings DESC LIMIT ?";
                params = new Object[]{limit};
            } else {
                sql = "SELECT n.moduleID, n.title, AVG(r.rating) as averageRating, COUNT(r.rating) as totalRatings " +
                      "FROM Notes n LEFT JOIN RatingStatistics r ON n.moduleID = r.moduleID " +
                      "GROUP BY n.moduleID, n.title HAVING COUNT(r.rating) >= 3 " +
                      "ORDER BY averageRating DESC, totalRatings DESC LIMIT ?";
                params = new Object[]{limit};
            }
            
            List<Map<String, Object>> topRated = jdbcTemplate.queryForList(sql, params);

            return ResponseEntity.ok().body(Map.of(
                    "success", "true",
                    "message", "Top rated notes retrieved successfully",
                    "data", topRated
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", "false",
                    "message", "Failed to retrieve top rated notes: " + e.getMessage()
            ));
        }
    }

    /**
     * Delete a rating
     */
    @DeleteMapping("/ratings/{ratingId}")
    public ResponseEntity<Map<String, String>> deleteRating(@PathVariable Long ratingId, HttpServletRequest request, HttpSession session) {
        // Check if user is logged in
        if (!security.isSessionValid(session, request)) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not logged in"));
        }

        try {
            statisticService.deleteRating(ratingId);

            return ResponseEntity.ok().body(Map.of(
                    "success", "true",
                    "message", "Rating deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", "false",
                    "message", "Failed to delete rating: " + e.getMessage()
            ));
        }
    }

    // ==================== BOOKMARKS ENDPOINTS ====================
    
    /**
     * Get bookmark statistics
     */
    @GetMapping("/bookmarks")
    public ResponseEntity<Map<String, Object>> getBookmarkStatistics(
            @RequestParam(required = false) String moduleID,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String period,
            HttpServletRequest request, HttpSession session) {
        
        // Check if user is logged in
        if (!security.isSessionValid(session, request)) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not logged in"));
        }

        String currentUser = (String) session.getAttribute("username");

        try {
            String sql;
            Object[] params;
            
            if (moduleID != null) {
                // Get bookmark count for specific note
                if (period != null) {
                    sql = "SELECT COUNT(*) as bookmarkCount FROM BookmarkStatistics WHERE moduleID = ? AND bookmarkedAt >= " + getPeriodCondition(period);
                    params = new Object[]{Integer.parseInt(moduleID)};
                } else {
                    sql = "SELECT COUNT(*) as bookmarkCount FROM BookmarkStatistics WHERE moduleID = ?";
                    params = new Object[]{Integer.parseInt(moduleID)};
                }
                
                Map<String, Object> result = jdbcTemplate.queryForMap(sql, params);
                
                // Check if current user has bookmarked this note
                String checkUserBookmarkSql = "SELECT COUNT(*) FROM BookmarkStatistics WHERE moduleID = ? AND username = ?";
                int userBookmarked = jdbcTemplate.queryForObject(checkUserBookmarkSql, Integer.class, Integer.parseInt(moduleID), currentUser);
                result.put("isBookmarkedByUser", userBookmarked > 0);
                
                return ResponseEntity.ok().body(Map.of(
                    "message", "Bookmark statistics retrieved successfully",
                    "data", result
                ));
                
            } else if (username != null) {
                // Get user's bookmarked notes
                if (period != null) {
                    sql = "SELECT n.moduleID, n.title, n.course, n.major, b.bookmarkedAt " +
                          "FROM BookmarkStatistics b JOIN Notes n ON b.moduleID = n.moduleID " +
                          "WHERE b.username = ? AND b.bookmarkedAt >= " + getPeriodCondition(period) + " " +
                          "ORDER BY b.bookmarkedAt DESC";
                } else {
                    sql = "SELECT n.moduleID, n.title, n.course, n.major, b.bookmarkedAt " +
                          "FROM BookmarkStatistics b JOIN Notes n ON b.moduleID = n.moduleID " +
                          "WHERE b.username = ? ORDER BY b.bookmarkedAt DESC";
                }
                params = new Object[]{username};
                
                List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, params);
                return ResponseEntity.ok().body(Map.of(
                    "message", "User bookmarks retrieved successfully",
                    "data", results
                ));
                
            } else {
                // Get most bookmarked notes
                if (period != null) {
                    sql = "SELECT n.moduleID, n.title, COUNT(b.moduleID) as bookmarkCount " +
                          "FROM Notes n LEFT JOIN BookmarkStatistics b ON n.moduleID = b.moduleID " +
                          "WHERE b.bookmarkedAt >= " + getPeriodCondition(period) + " OR b.bookmarkedAt IS NULL " +
                          "GROUP BY n.moduleID, n.title ORDER BY bookmarkCount DESC LIMIT 10";
                } else {
                    sql = "SELECT n.moduleID, n.title, COUNT(b.moduleID) as bookmarkCount " +
                          "FROM Notes n LEFT JOIN BookmarkStatistics b ON n.moduleID = b.moduleID " +
                          "GROUP BY n.moduleID, n.title ORDER BY bookmarkCount DESC LIMIT 10";
                }
                
                List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
                return ResponseEntity.ok().body(Map.of(
                    "message", "Most bookmarked notes retrieved successfully",
                    "data", results
                ));
            }
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error retrieving bookmark statistics: " + e.getMessage()));
        }
    }

    /**
     * Add or remove bookmark
     */
    @PostMapping("/bookmarks")
    public ResponseEntity<Map<String, String>> toggleBookmark(
            @RequestBody Map<String, String> requestData,
            HttpServletRequest request, HttpSession session) {
        
        // Check if user is logged in
        if (!security.isSessionValid(session, request)) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not logged in"));
        }

        String username = (String) session.getAttribute("username");
        String moduleIdStr = requestData.get("moduleID");

        if (moduleIdStr == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Module ID is required"));
        }

        try {
            int moduleID = Integer.parseInt(moduleIdStr);
            
            // Check if note exists
            String checkNoteSql = "SELECT COUNT(*) FROM Notes WHERE moduleID = ?";
            int noteCount = jdbcTemplate.queryForObject(checkNoteSql, Integer.class, moduleID);
            
            if (noteCount == 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Note not found"));
            }

            // Check if bookmark already exists
            String checkBookmarkSql = "SELECT COUNT(*) FROM BookmarkStatistics WHERE moduleID = ? AND username = ?";
            int bookmarkCount = jdbcTemplate.queryForObject(checkBookmarkSql, Integer.class, moduleID, username);
            
            if (bookmarkCount > 0) {
                // Remove bookmark
                String deleteSql = "DELETE FROM BookmarkStatistics WHERE moduleID = ? AND username = ?";
                jdbcTemplate.update(deleteSql, moduleID, username);
                return ResponseEntity.ok().body(Map.of("message", "Bookmark removed successfully", "isBookmarked", "false"));
            } else {
                // Add bookmark
                String insertSql = "INSERT INTO BookmarkStatistics (moduleID, username, bookmarkedAt) VALUES (?, ?, NOW())";
                jdbcTemplate.update(insertSql, moduleID, username);
                return ResponseEntity.ok().body(Map.of("message", "Note bookmarked successfully", "isBookmarked", "true"));
            }
            
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid module ID format"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error toggling bookmark: " + e.getMessage()));
        }
    }

    /**
     * Get user's bookmarked notes
     */
    @GetMapping("/bookmarks/user")
    public ResponseEntity<Map<String, Object>> getUserBookmarks(
            HttpServletRequest request, HttpSession session) {
        
        // Check if user is logged in
        if (!security.isSessionValid(session, request)) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not logged in"));
        }

        String username = (String) session.getAttribute("username");

        try {
            String sql = "SELECT n.moduleID, n.title, n.course, n.major, n.description, b.bookmarkedAt " +
                        "FROM BookmarkStatistics b JOIN Notes n ON b.moduleID = n.moduleID " +
                        "WHERE b.username = ? ORDER BY b.bookmarkedAt DESC";
            
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, username);
            
            return ResponseEntity.ok().body(Map.of(
                "message", "User bookmarks retrieved successfully",
                "data", results,
                "count", results.size()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error retrieving user bookmarks: " + e.getMessage()));
        }
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

    // ==================== HELPER METHODS ====================
    
    /**
     * Helper method to get SQL condition for time periods
     */
    private String getPeriodCondition(String period) {
        switch (period.toLowerCase()) {
            case "today":
                return "CURDATE()";
            case "week":
                return "DATE_SUB(NOW(), INTERVAL 1 WEEK)";
            case "month":
                return "DATE_SUB(NOW(), INTERVAL 1 MONTH)";
            case "year":
                return "DATE_SUB(NOW(), INTERVAL 1 YEAR)";
            default:
                return "DATE_SUB(NOW(), INTERVAL 1 WEEK)";
        }
    }
}
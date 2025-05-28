package com.opennotepad.controller;

import com.opennotepad.dto.StatisticResponse;
import com.opennotepad.dto.ViewStatisticRequest;
import com.opennotepad.dto.RatingStatisticRequest;
import com.opennotepad.dto.BookmarkStatisticRequest;
import com.opennotepad.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "*")
public class StatisticController {

    @Autowired
    private StatisticService statisticService;

    // ==================== VIEWS ENDPOINTS ====================
    
    /**
     * Get view statistics for all notes or specific note
     */
    @GetMapping("/views")
    public ResponseEntity<StatisticResponse> getViewStatistics(
            @RequestParam(required = false) Long noteId,
            @RequestParam(required = false) String period,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Map<String, Object> viewStats = statisticService.getViewStatistics(noteId, period, page, size);
            
            StatisticResponse response = StatisticResponse.builder()
                    .success(true)
                    .message("View statistics retrieved successfully")
                    .data(viewStats)
                    .build();
                    
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            StatisticResponse response = StatisticResponse.builder()
                    .success(false)
                    .message("Failed to retrieve view statistics: " + e.getMessage())
                    .build();
                    
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Record a new view for a note
     */
    @PostMapping("/views")
    public ResponseEntity<StatisticResponse> recordView(@Valid @RequestBody ViewStatisticRequest request) {
        try {
            statisticService.recordView(request);
            
            StatisticResponse response = StatisticResponse.builder()
                    .success(true)
                    .message("View recorded successfully")
                    .build();
                    
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            StatisticResponse response = StatisticResponse.builder()
                    .success(false)
                    .message("Failed to record view: " + e.getMessage())
                    .build();
                    
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get top viewed notes
     */
    @GetMapping("/views/top")
    public ResponseEntity<StatisticResponse> getTopViewedNotes(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String period) {
        
        try {
            List<Map<String, Object>> topViewed = statisticService.getTopViewedNotes(limit, period);
            
            StatisticResponse response = StatisticResponse.builder()
                    .success(true)
                    .message("Top viewed notes retrieved successfully")
                    .data(Map.of("topViewedNotes", topViewed))
                    .build();
                    
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            StatisticResponse response = StatisticResponse.builder()
                    .success(false)
                    .message("Failed to retrieve top viewed notes: " + e.getMessage())
                    .build();
                    
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ==================== RATINGS ENDPOINTS ====================
    
    /**
     * Get rating statistics
     */
    @GetMapping("/ratings")
    public ResponseEntity<StatisticResponse> getRatingStatistics(
            @RequestParam(required = false) Long noteId,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Map<String, Object> ratingStats = statisticService.getRatingStatistics(noteId, userId, page, size);
            
            StatisticResponse response = StatisticResponse.builder()
                    .success(true)
                    .message("Rating statistics retrieved successfully")
                    .data(ratingStats)
                    .build();
                    
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            StatisticResponse response = StatisticResponse.builder()
                    .success(false)
                    .message("Failed to retrieve rating statistics: " + e.getMessage())
                    .build();
                    
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Submit or update a rating
     */
    @PostMapping("/ratings")
    public ResponseEntity<StatisticResponse> submitRating(@Valid @RequestBody RatingStatisticRequest request) {
        try {
            statisticService.submitRating(request);
            
            StatisticResponse response = StatisticResponse.builder()
                    .success(true)
                    .message("Rating submitted successfully")
                    .build();
                    
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            StatisticResponse response = StatisticResponse.builder()
                    .success(false)
                    .message("Failed to submit rating: " + e.getMessage())
                    .build();
                    
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get top rated notes
     */
    @GetMapping("/ratings/top")
    public ResponseEntity<StatisticResponse> getTopRatedNotes(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String period) {
        
        try {
            List<Map<String, Object>> topRated = statisticService.getTopRatedNotes(limit, period);
            
            StatisticResponse response = StatisticResponse.builder()
                    .success(true)
                    .message("Top rated notes retrieved successfully")
                    .data(Map.of("topRatedNotes", topRated))
                    .build();
                    
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            StatisticResponse response = StatisticResponse.builder()
                    .success(false)
                    .message("Failed to retrieve top rated notes: " + e.getMessage())
                    .build();
                    
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Delete a rating
     */
    @DeleteMapping("/ratings/{ratingId}")
    public ResponseEntity<StatisticResponse> deleteRating(@PathVariable Long ratingId) {
        try {
            statisticService.deleteRating(ratingId);
            
            StatisticResponse response = StatisticResponse.builder()
                    .success(true)
                    .message("Rating deleted successfully")
                    .build();
                    
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            StatisticResponse response = StatisticResponse.builder()
                    .success(false)
                    .message("Failed to delete rating: " + e.getMessage())
                    .build();
                    
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ==================== BOOKMARKS ENDPOINTS ====================
    
    /**
     * Get bookmark statistics
     */
    @GetMapping("/bookmarks")
    public ResponseEntity<StatisticResponse> getBookmarkStatistics(
            @RequestParam(required = false) Long noteId,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Map<String, Object> bookmarkStats = statisticService.getBookmarkStatistics(noteId, userId, page, size);
            
            StatisticResponse response = StatisticResponse.builder()
                    .success(true)
                    .message("Bookmark statistics retrieved successfully")
                    .data(bookmarkStats)
                    .build();
                    
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            StatisticResponse response = StatisticResponse.builder()
                    .success(false)
                    .message("Failed to retrieve bookmark statistics: " + e.getMessage())
                    .build();
                    
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Add or remove bookmark
     */
    @PostMapping("/bookmarks")
    public ResponseEntity<StatisticResponse> toggleBookmark(@Valid @RequestBody BookmarkStatisticRequest request) {
        try {
            boolean isBookmarked = statisticService.toggleBookmark(request);
            
            String message = isBookmarked ? "Note bookmarked successfully" : "Bookmark removed successfully";
            
            StatisticResponse response = StatisticResponse.builder()
                    .success(true)
                    .message(message)
                    .data(Map.of("isBookmarked", isBookmarked))
                    .build();
                    
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            StatisticResponse response = StatisticResponse.builder()
                    .success(false)
                    .message("Failed to toggle bookmark: " + e.getMessage())
                    .build();
                    
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get most bookmarked notes
     */
    @GetMapping("/bookmarks/top")
    public ResponseEntity<StatisticResponse> getMostBookmarkedNotes(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String period) {
        
        try {
            List<Map<String, Object>> mostBookmarked = statisticService.getMostBookmarkedNotes(limit, period);
            
            StatisticResponse response = StatisticResponse.builder()
                    .success(true)
                    .message("Most bookmarked notes retrieved successfully")
                    .data(Map.of("mostBookmarkedNotes", mostBookmarked))
                    .build();
                    
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            StatisticResponse response = StatisticResponse.builder()
                    .success(false)
                    .message("Failed to retrieve most bookmarked notes: " + e.getMessage())
                    .build();
                    
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get user's bookmarked notes
     */
    @GetMapping("/bookmarks/user/{userId}")
    public ResponseEntity<StatisticResponse> getUserBookmarks(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Map<String, Object> userBookmarks = statisticService.getUserBookmarks(userId, page, size);
            
            StatisticResponse response = StatisticResponse.builder()
                    .success(true)
                    .message("User bookmarks retrieved successfully")
                    .data(userBookmarks)
                    .build();
                    
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            StatisticResponse response = StatisticResponse.builder()
                    .success(false)
                    .message("Failed to retrieve user bookmarks: " + e.getMessage())
                    .build();
                    
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ==================== GENERAL STATISTICS ENDPOINTS ====================
    
    /**
     * Get overall statistics dashboard
     */
    @GetMapping("/dashboard")
    public ResponseEntity<StatisticResponse> getDashboardStatistics() {
        try {
            Map<String, Object> dashboardStats = statisticService.getDashboardStatistics();
            
            StatisticResponse response = StatisticResponse.builder()
                    .success(true)
                    .message("Dashboard statistics retrieved successfully")
                    .data(dashboardStats)
                    .build();
                    
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            StatisticResponse response = StatisticResponse.builder()
                    .success(false)
                    .message("Failed to retrieve dashboard statistics: " + e.getMessage())
                    .build();
                    
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
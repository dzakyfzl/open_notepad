package com.kelompok5.open_notepad.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/interface")
@Validated
public class InterfaceController {

    // DTO untuk rateNote
    public static class RateRequest {
        @NotBlank(message = "UserID tidak boleh kosong")
        public String userID;

        @NotBlank(message = "ModuleID tidak boleh kosong")
        public String moduleID;

        @Min(value = 1, message = "Rating harus minimal 1")
        @Max(value = 5, message = "Rating harus maksimal 5")
        public int rating;
    }

    // GET /interface/downloadnote?userID=...&moduleID=...
    @GetMapping("/downloadnote")
    public ResponseEntity<?> downloadNote(
            @RequestParam String userID,
            @RequestParam String moduleID
    ) {
        // Logika download (misal simpan ke DB) di sini

        String message = "Catatan dengan ID " + moduleID + " berhasil diunduh oleh " + userID;
        return ResponseEntity.ok(new ApiResponse("success", message));
    }

    // POST /interface/rate dengan body JSON
    @PostMapping("/rate")
    public ResponseEntity<?> rateNote(@Valid @RequestBody RateRequest request) {
        // Logika simpan rating ke DB di sini

        String message = "Rating " + request.rating + " berhasil disimpan untuk catatan " + request.moduleID + " oleh " + request.userID;
        return ResponseEntity.ok(new ApiResponse("success", message));
    }

    // POST /interface/bookmark?userID=...&moduleID=...
    @PostMapping("/bookmark")
    public ResponseEntity<?> bookmarkNote(
            @RequestParam String userID,
            @RequestParam String moduleID
    ) {
        // Logika bookmark di sini

        String message = "Catatan " + moduleID + " berhasil disimpan oleh " + userID;
        return ResponseEntity.ok(new ApiResponse("success", message));
    }

    // Response wrapper class
    public static class ApiResponse {
        private String status;
        private String message;

        public ApiResponse(String status, String message) {
            this.status = status;
            this.message = message;
        }
        public String getStatus() {
            return status;
        }
        public String getMessage() {
            return message;
        }
    }
}

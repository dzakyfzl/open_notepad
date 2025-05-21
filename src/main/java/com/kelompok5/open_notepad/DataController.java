package com.kelompok5.open_notepad;

import java.util.List;
import java.util.Map;

import com.kelompok5.open_notepad.DAO.ModuleDAO;
import com.kelompok5.open_notepad.entity.File;
import com.kelompok5.open_notepad.entity.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping("/api/data")

public class DataController {
    @Autowired
    private ModuleDAO moduleDAO;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    // Example method to save a user

    @GetMapping("/getAllModules")
    public ResponseEntity<Map<String,String>> getAllModules() {
        Module module = new Module();
        moduleDAO.
        return ResponseEntity.ok(Map.of("message", "success"));
    }
}

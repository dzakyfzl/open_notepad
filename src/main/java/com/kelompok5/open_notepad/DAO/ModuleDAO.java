package com.kelompok5.open_notepad.DAO;

import com.kelompok5.open_notepad.entity.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ModuleDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public List<Module> getAllModules() {
        String sql = "SELECT * FROM Modules WHERE visibility = 1";
        return jdbcTemplate.query(sql, moduleRowMapper);
    }

    private RowMapper<Module> moduleRowMapper = (rs, rowNum) -> {
        ;
        
    };
    public Module getFromDatabase(String moduleID){
        //get module from database logic
        
        module.setTitle(rs.getString("name"));
        module.setDescription(rs.getString("description"));
        module.setCourse(rs.getString("course"));
        module.setMajor(rs.getString("major"));
        module.setUploadDate(rs.getDate("dateUploaded"));
        module.setVisibility(rs.getBoolean("visibility"));
        // Anda bisa tambahkan setter lain jika ada di class Module
        return module;
    }

    public void uploadToDatabase() {
        //upload module to database logic
    }
}
package com.kelompok5.open_notepad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
//import javax.sql.DataSource;
//import java.sql.Connection;

@SpringBootApplication
public class OpenNotepadApplication {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(OpenNotepadApplication.class, args);
	}
}
// 	@PostConstruct
//     public void testConnection() {
//          result = jdbcTemplate.queryForObject("SELECT * FROM Accounts", Integer.class);
//         System.out.println("Koneksi JDBC berhasil, hasil query: " + result);
//     }
// }

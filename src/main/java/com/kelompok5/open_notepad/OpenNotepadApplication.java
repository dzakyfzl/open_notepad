package com.kelompok5.open_notepad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.jdbc.core.JdbcTemplate;
//import javax.sql.DataSource;
//import java.sql.Connection;

@SpringBootApplication
public class OpenNotepadApplication {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		io.github.cdimascio.dotenv.Dotenv dotenv = io.github.cdimascio.dotenv.Dotenv.load();
        System.setProperty("SQLusername", dotenv.get("SQLusername"));
        System.setProperty("SQLpassword", dotenv.get("SQLpassword"));
		SpringApplication.run(OpenNotepadApplication.class, args);
	}
}
// 	@PostConstruct
//     public void testConnection() {
//          result = jdbcTemplate.queryForObject("SELECT * FROM Accounts", Integer.class);
//         System.out.println("Koneksi JDBC berhasil, hasil query: " + result);
//     }
// }

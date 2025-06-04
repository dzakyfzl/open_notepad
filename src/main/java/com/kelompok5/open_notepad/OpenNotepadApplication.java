package com.kelompok5.open_notepad;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class OpenNotepadApplication {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		String profile = getActiveProfile(args);

		if(!"prod".equalsIgnoreCase(profile)){
			Dotenv dotenv = Dotenv.configure().directory("./").ignoreIfMissing().load();
        	setPropertyIfPresent("SQLusername", dotenv.get("SQLusername"));
        	setPropertyIfPresent("SQLpassword", dotenv.get("SQLpassword"));
		}else{
			setPropertyIfPresent("SQLusername", System.getenv("SQLusername"));
			setPropertyIfPresent("SQLpassword", System.getenv("SQLpassword"));
		}
		SpringApplication.run(OpenNotepadApplication.class, args);
	}

	private static String getActiveProfile(String[] args) {
        return Arrays.stream(args)
                .filter(arg -> arg.startsWith("--profiles="))
                .map(arg -> arg.split("=")[1])
                .findFirst()
                .orElse(System.getenv().getOrDefault("PROFILES", "default"));
    }

    private static void setPropertyIfPresent(String key, String value) {
        if (value != null && !value.isBlank()) {
            System.setProperty(key, value);
        }
    }
}

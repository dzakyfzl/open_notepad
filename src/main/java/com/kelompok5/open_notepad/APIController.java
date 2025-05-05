package com.kelompok5.open_notepad;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.WebSession;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import org.springframework.http.ResponseEntity;





@RestController
@RequestMapping("/api")
public class APIController {


    @PostMapping("/auth/signin")
    public Mono<ResponseEntity<Map<String,String>>> signin(@RequestBody Map<String, String> request, WebSession session) {
        String username = request.get("username");
        String password = request.get("password");
        session.getAttributes().put("username", username);
        return Mono.just(ResponseEntity.ok().body(Map.of("username", username)));
    }
    @PostMapping("/auth/signup")
    public Mono<ResponseEntity<Map<String,String>>> signUp(@RequestBody Map<String, String> request, WebSession session) {
        String username = request.get("username");
        String password = request.get("password");
        session.getAttributes().put("username", username);
        return Mono.just(ResponseEntity.ok().body(Map.of("username", username)));
    }
}

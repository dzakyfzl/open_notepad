package com.kelompok5.open_notepad.DAO;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kelompok5.open_notepad.entity.Session;

@Component
public class SessionDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    List<Map<String,Object>> sessionCache;


    public void uploadToDatabase(String sessionID, String username, String userAgent) {
        // set session logic
        // Querry inserting to database
        if(sessionCache == null){
            sessionCache = databaseToCache();
        }
        System.out.println("Get from cache : " + sessionCache.size() + " session in cache");
        sessionCache.add(Map.of("sessionID",sessionID,"username",username,"userAgent",userAgent,"dateCreated",new Date(System.currentTimeMillis())));
    }

    public void deleteSession(String username) {
        if(sessionCache != null){
            System.out.println("Get from cache : " + sessionCache.size() + " session in cache");
            for(int i = 0; i < sessionCache.size(); i++){
                if(sessionCache.get(i).get("username").equals(username)){
                    System.out.println("Removed");
                    sessionCache.remove(i);
                    break;
                }
            }
        }else{
            sessionCache = databaseToCache();
        }
    }

    public Session getFromDatabase(String username) {
        if(sessionCache != null){
            System.out.println("Get from cache : " + sessionCache.size() + " session in cache");
            for(Map<String,Object> session : sessionCache){
                if(session.get("username").equals(username)){
                    return new Session((String)session.get("sessionID"),(String)session.get("username"),(String)session.get("userAgent"),(Date)session.get("dateCreated"));
                }
            }
        }
        return null;
    }

    private List<Map<String,Object>> databaseToCache() {
        return jdbcTemplate.queryForList("SELECT * FROM Sessions");
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void cacheToDatabase() {
        jdbcTemplate.update("DELETE FROM Sessions");
        for(Map<String,Object> session : sessionCache){
            jdbcTemplate.update("INSERT INTO Sessions (sessionID, username, userAgent, dateCreated) VALUES (?,?,?,?)",session.get("sessionID"),session.get("username"),session.get("userAgent"),session.get("dateCreated"));
        }
        sessionCache = null;
    }
}

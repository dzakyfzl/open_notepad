package com.kelompok5.open_notepad.DAO;

import java.sql.Date;
import java.sql.Timestamp;
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
        System.out.println("Delete session");
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
                Object dateObj = session.get("dateCreated");
                Date dateCreated;
                if (dateObj instanceof Timestamp timestamp) {
                    dateCreated = new Date(timestamp.getTime());
                } else if (dateObj instanceof Date date) {
                    dateCreated = date;
                } else{
                    throw new IllegalArgumentException("Unsupported date type: " + dateObj.getClass().getName());
                }

                if(session.get("username").equals(username)){
                    System.out.println("is approved");
                    Session session1 = new Session((String)session.get("sessionID"),(String)session.get("username"),(String)session.get("userAgent"),dateCreated);
                    System.out.println(session1.getDateCreated());
                    return session1;
                }
            }
        }else{
            sessionCache = databaseToCache();
        }
        return null;
    }

    private List<Map<String,Object>> databaseToCache() {
        List<Map<String,Object>> returnVal = jdbcTemplate.queryForList("SELECT sessionID, username, userAgent, dateCreated FROM Sessions");
        return returnVal;
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

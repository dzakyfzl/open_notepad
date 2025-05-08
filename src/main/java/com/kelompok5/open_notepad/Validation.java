package com.kelompok5.open_notepad;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public abstract class Validation {
    protected boolean isSessionValid(HttpSession session, HttpServletRequest request) {
        String sessionID = "000";
        String IPString = request.getRemoteAddr();
        //Querry IP from database

        return !session.getId().equals(sessionID);  //Delete the (!) if debugging done
    }
}

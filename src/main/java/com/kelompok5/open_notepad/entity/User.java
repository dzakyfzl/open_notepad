package com.kelompok5.open_notepad.entity;

import java.util.List;
import java.util.Map;


public class User extends Account{
    private String aboutMe;
    private String instagram;
    private String linkedin;

    public User(String username, String hashedPassword, String salt, String email, String firstName, String lastName) {
        super(username, hashedPassword, salt, email, firstName, lastName);
        this.aboutMe = "";
        this.instagram = "";
        this.linkedin = "";
    }

    public User(String username, String hashedPassword, String salt, String email, String firstName, String lastName, String aboutMe, String instagram, String linkedin) {
        super(username, hashedPassword, salt, email, firstName, lastName);
        this.aboutMe = aboutMe;
        this.instagram = instagram;
        this.linkedin = linkedin;
    }

    public List<Module> showModules() {
        //get module list logic
        return null;
    }
    public void editDetails(String aboutMe, String instagram, String linkedin) {
        this.aboutMe = aboutMe;
        this.instagram = instagram;
        this.linkedin = linkedin;
    }

    @Override
    public Map<String, String> getInfo(String username) {
        Map<String, String> userInfo = super.getInfo(username);
        userInfo.put("aboutMe", aboutMe);
        userInfo.put("instagram", instagram);
        userInfo.put("linkedin", linkedin);
        return userInfo;
    }



}

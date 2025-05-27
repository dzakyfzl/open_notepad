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
        if (this.aboutMe != null) {
            userInfo.put("aboutMe", this.aboutMe);
        } else {
            userInfo.put("aboutMe", " ");
        }
        if (this.instagram != null) {
            userInfo.put("instagram", this.instagram);
        } else {
            userInfo.put("instagram", " ");
        }
        if (this.linkedin != null) {
            userInfo.put("linkedin", this.linkedin);
        } else {
            userInfo.put("linkedin", " ");
        }
        System.out.println("User Info from : " + username);
        return userInfo;
    }

    public String getAboutMe() {
        if (aboutMe == null) {
            return " ";
        }
        return aboutMe;
    }
    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }
    public String getInstagram() {
        if (instagram == null) {
            return " ";
        }
        return instagram;
    }
    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }
    public String getLinkedin() {
        if (linkedin == null) {
            return " ";
        }
        return linkedin;
    }
    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }



}

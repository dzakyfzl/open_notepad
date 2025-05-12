package com.kelompok5.open_notepad.entity;

import java.util.List;
import java.util.Map;


public class User extends Account{
    private String aboutMe;
    private String instagram;
    private String linkedin;

    public void uploadModule(Module module) {
        //upload module logic
        //save module to database
    }
    public Module downloadModule(Module module) {
        //download module logic
        //get module from database
        //return module
        return null;
    }
    public void editModule(Module module) {
        //edit module logic
        //check if module is owned by user
        //update module in database

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
    public void deleteModule(Module module) {
        //delete module logic
        //check if module is owned by user
        //remove module from database
    }

    @Override
    public Map<String, String> getInfo(String username) {
        Map<String, String> userInfo = super.getInfo(username);
        userInfo.put("aboutMe", aboutMe);
        userInfo.put("instagram", instagram);
        userInfo.put("linkedin", linkedin);
        return userInfo;
    }

    @Override
    public void uploadToDatabase() {
        super.uploadToDatabase();
        //upload user to database logic
        //save user to database
    }
    
}

package com.kelompok5.open_notepad.entity;


public class User extends Account {
    public User(String username, String hashedPassword, String salt, String email, String firstName, String lastName) {
        super(username, hashedPassword, salt, email, firstName, lastName);
    }
    public void uploadModule(Module module) {
        //upload module logic
    }
    public void downloadModule(Module module) {
        //download module logic
    }
    public void deleteModule(Module module) {
        //delete module logic
    }
    public void editModule(Module module) {
        //edit module logic
    }
    public void findModule(Module module) {
        //edit module logic
    }
    
}

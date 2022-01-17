package com.example.scavengerhunt.Entities;

public class Scavenger {

    private User user;
    //private String activeSessionId;
    private long aLat;
    private long aLong;

    //public enum Role {NAVIGATOR, COLLECTOR, SPECTATOR}
    public String role;
    //private Role role;

    public Scavenger(User user){
        this.role = "Spectator";
        this.user = user;
        //this.activeSessionId = sessionId;
    }

    public Scavenger(){

    }

    public User getUser(){
        return user;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /*public String getUserName(){
        return User.getInstance().getName();
    }*/

    public String getRole(){
        return role;
    }


}

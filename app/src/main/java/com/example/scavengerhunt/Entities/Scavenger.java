package com.example.scavengerhunt.Entities;

public class Scavenger {

    private User user;
    private String activeSessionId;
    private long aLat;
    private long aLong;

    public enum Role {NAVIGATOR, COLLECTOR}
    private Role role;

    public Scavenger(){

    }

    public User getUser(){
        return user;
    }

    public void setRole(Role role) {
        this.role = role;
    }


}

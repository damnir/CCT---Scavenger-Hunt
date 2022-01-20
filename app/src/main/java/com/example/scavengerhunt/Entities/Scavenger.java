package com.example.scavengerhunt.Entities;

public class Scavenger {

    private static Scavenger INSTANCE = new Scavenger();


    private User user;
    //private String activeSessionId;
    private long aLat;
    private long aLong;

    //public enum Role {NAVIGATOR, COLLECTOR, SPECTATOR}
    public String role;
    //private Role role;
    private int scavengerId;

    public Scavenger(User user){
        this.role = "Spectator";
        this.user = user;
        //this.activeSessionId = sessionId;
    }

    public Scavenger(){
        this.role = "Spectator";
    }

    public User getUser(){
        return user;
    }

    public void setUser(User mUser) {
        this.user = mUser;
    }

    public static Scavenger getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Scavenger();
        }
        return INSTANCE;
    }

    public void setScavengerId(int newId) { this.scavengerId = newId; }
    public int getScavengerId() { return this.scavengerId; }

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

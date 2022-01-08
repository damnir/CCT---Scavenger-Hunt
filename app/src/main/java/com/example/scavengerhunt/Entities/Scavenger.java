package com.example.scavengerhunt.Entities;

public class Scavenger {

    private User user;
    private String activeSessionId;
    private long aLat;
    private long aLong;

    public enum Role {NAVIGATOR, COLLECTOR, SPECTATOR}
    private Role role;

    public Scavenger(User user, String sessionId){
        this.role = Role.SPECTATOR;
        this.user = user;
        this.activeSessionId = sessionId;
    }

    public User getUser(){
        return user;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUserName(){
        return User.getInstance().getName();
    }

    public String getRole(){
        switch (role) {
            case COLLECTOR:
                return "Collector";
            case NAVIGATOR:
                return "Navigator";
            case SPECTATOR:
                return "Spectator";
        }
        return null;
    }


}

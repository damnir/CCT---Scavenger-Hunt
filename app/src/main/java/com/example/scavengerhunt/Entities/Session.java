package com.example.scavengerhunt.Entities;

import android.util.Log;

import com.example.scavengerhunt.Firebase.Database;

import java.util.ArrayList;
import java.util.List;

public class Session {

    private static Session INSTANCE = new Session("");

    private String sessionId;
    private User sessionOwner;
    private List<Scavenger> scavengers;

    private Database dbRef = Database.getInstance();

    public Session(String id) {
        scavengers = new ArrayList<>();

        dbRef = Database.getInstance();

        //this.sessionId = id;
        //this.sessionOwner = User.getInstance();

        //this.scavengers.add(new Scavenger(sessionOwner, sessionId));

        //dbRef.newSession(this);
    }

    public Session(){

    }

    public void replaceInstance(Session instance) {
        INSTANCE = instance;
    }

    public static Session getInstance() {
        if (INSTANCE == null)
        {
            INSTANCE = new Session();
        }
        return INSTANCE;
    }

    public void setSessionId(String id){
        this.sessionId = id;
    }

    public void addScavenger(Scavenger scavenger) {
        scavenger.setScavengerId(scavengers.size());
        scavengers.add(scavenger);
    }

    public void setScavengers(List<Scavenger> newScavengers) {
        this.scavengers = newScavengers;
    }

    public User getOwner() { return sessionOwner; }

    public void setOwner(User user) {
        this.sessionOwner = user;
    }

    public String getSessionId() { return sessionId; }

    public List<Scavenger> getScavengers() {
        return scavengers;
    }

    public void updateRole(int scavengerId, String role) {
        scavengers.get(scavengerId).setRole(role);
        Log.d("SC", "user instance: " + User.getInstance().getActiveSessionId() + " scavengerId: " + scavengerId + " role:" + role);
        Log.d("SC", "dbref: " + dbRef.test);
        dbRef.updateRole(User.getInstance().getActiveSessionId(), scavengerId, role);
    }






}

package com.example.scavengerhunt.Entities;

import com.example.scavengerhunt.Firebase.Database;

import java.util.ArrayList;
import java.util.List;

public class Session {

    private String sessionId;
    private User sessionOwner;
    private List<Scavenger> scavengers;

    private Database dbRef;

    public Session(String id) {
        scavengers = new ArrayList<>();

        dbRef = Database.getInstance();

        this.sessionId = id;
        this.sessionOwner = User.getInstance();

        this.scavengers.add(new Scavenger(sessionOwner, sessionId));

        dbRef.newSession(this);
    }

    public void addScavenger(Scavenger scavenger) {
        scavengers.add(scavenger);
    }

    public User getOwner() { return sessionOwner; }

    public String getSessionId() { return sessionId; }

    public List<Scavenger> getScavengers() {
        return scavengers;
    }






}

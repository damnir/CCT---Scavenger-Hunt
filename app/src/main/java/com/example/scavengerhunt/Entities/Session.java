package com.example.scavengerhunt.Entities;

//import android.util.Log;

import com.example.scavengerhunt.Firebase.Database;

import java.util.ArrayList;
import java.util.List;

public class Session {

    private static Session INSTANCE = new Session("");

    private String sessionId;
    private User sessionOwner;
    private List<Scavenger> scavengers;
    public List<Log> logs;
    public List<Artifact> artifactList;
    private List<Site> siteList;
    public List<Story> stories;
    public List<Message> messages;

    private Database dbRef = Database.getInstance();

    public Session(String id) {
        scavengers = new ArrayList<>();
        logs = new ArrayList<>();
        artifactList = new ArrayList<>();
        siteList = new ArrayList<>();
        stories = new ArrayList<>();
        messages = new ArrayList<>();
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

    public User getOwner() { return this.sessionOwner; }

    public void setOwner(User user) {
        this.sessionOwner = user;
    }

    public String getSessionId() { return this.sessionId; }

    public List<Scavenger> getScavengers() {
        return this.scavengers;
    }

    public void updateRole(int scavengerId, String role) {
        scavengers.get(scavengerId).setRole(role);
        //Log.d("SC", "user instance: " + User.getInstance().getActiveSessionId() + " scavengerId: " + scavengerId + " role:" + role);
        //Log.d("SC", "dbref: " + dbRef.test);
        dbRef.updateRole(User.getInstance().getActiveSessionId(), scavengerId, role);
    }



    public void setLogs(List<Log> nLogs) {
        this.logs = nLogs;
    }
    public List<Log> getLogs() {
        return this.logs;
    }

    public void setMessages(List<Message> mMessages) {
        this.messages = mMessages;
    }
    public List<Message> getMessages() {
        return this.messages;
    }

    public void addLog(Log nLog) {
        logs.add(nLog);
    }

    public void addArtifact(Artifact artifact) { artifactList.add(artifact); }
    public void addSite(Site site) { siteList.add(site); }

    public void setSites(List<Site> sites) {
        this.siteList = sites;
    }
    public List<Site> getSites() {
        return this.siteList;
    }

    public void addStory(Story story) {
        stories.add(story);
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }
    public List<Story> getStories() {
        return this.stories;
    }

    public void addMessage(Message message) { messages.add(message); }

    public void setArtifactList(List<Artifact> list) { this.artifactList = list; }







}

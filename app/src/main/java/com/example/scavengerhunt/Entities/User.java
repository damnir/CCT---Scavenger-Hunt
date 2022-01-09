package com.example.scavengerhunt.Entities;

import android.util.Log;

import com.example.scavengerhunt.Firebase.Database;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User {

    private Database dbRef;

    private FirebaseUser fUser;
    private String id;
    private String name;
    private String email;
    private String activeSessionId;

    private static User INSTANCE = new User();

    public User() {
        dbRef = Database.getInstance();
    };

    public static User getInstance() {
        return INSTANCE;
    }

    public void setUser(FirebaseUser user) {
        this.id = user.getUid();
        this.email = user.getEmail();
        dbRef.readUsername(id);
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public FirebaseUser getfUser(){ return fUser; }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        Log.d("name", "Name: " + this.name);
        return this.name;
    }

    public void setActiveSessionId(String sessionId) {
        this.activeSessionId = sessionId;
    }

    public String getActiveSessionId() {
        return activeSessionId;
    }
}

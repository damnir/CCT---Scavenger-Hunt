package com.example.scavengerhunt.Entities;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User {

    FirebaseUser fUser;
    String id;
    String name;
    String email;

    private static User INSTANCE = new User();

    public User() {
    };

    public static User getInstance() {
        return INSTANCE;
    }

    public void setUser(FirebaseUser user) {
        this.id = user.getUid();
        this.email = user.getEmail();
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }



}

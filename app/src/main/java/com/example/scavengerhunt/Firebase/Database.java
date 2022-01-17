package com.example.scavengerhunt.Firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.scavengerhunt.Entities.Scavenger;
import com.example.scavengerhunt.Entities.Session;
import com.example.scavengerhunt.Entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.concurrent.atomic.AtomicReference;

public class Database {

    private static Database INSTANCE = new Database();

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Session session = Session.getInstance();


    public Database() {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    public static Database getInstance() {
        return INSTANCE;
    }

    public void writeNewUser(User user) {
        mDatabase.child("users").child(user.getId()).setValue(user);
        Log.d("DB", "ID: " + user.getId() + " email: " + user.getEmail());
    }

    public void changeName(String newName) {

        String uId = User.getInstance().getId();
        mDatabase.child("users").child(uId).child("name").setValue(newName);
    }

    public void readUsername(String uId) {
        mDatabase.child("users").child(uId).child("name").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            }
            else {
                Log.d("firebase", String.valueOf(task.getResult().getValue()));
                String name = String.valueOf(task.getResult().getValue());
                User.getInstance().setName(name);
            }
        });
    }

    public void newSession(Session session) {

        mDatabase.child("active_sessions").child(session.getSessionId()).setValue(session);

        //mDatabase.child("active_sessions").child(session.getSessionId())
        //        .child("owner").setValue(session.getOwnerId());

    }

    public void joinSession(String sessionId) {
        //TODO make sure session exists
        //User.getInstance().setActiveSessionId(sessionId);
        //mDatabase.child("active_sessions").child(sessionId).child("scavengers").child(scavenger.getUser().getId()).setValue(scavenger);
        mDatabase.child("active_sessions").child(sessionId).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            }
            else {
                session = task.getResult().getValue(Session.class);
            }
        });
        Log.d("SC", "Session ID: " + session.getSessionId());
        for (Scavenger s: session.getScavengers() ){
            Log.d("SC", "Scavengers: " + s.getUser().getName() + " role: " + s.getRole());

        }
    }

}

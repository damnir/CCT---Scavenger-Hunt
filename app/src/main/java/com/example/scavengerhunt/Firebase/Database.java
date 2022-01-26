package com.example.scavengerhunt.Firebase;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.scavengerhunt.Entities.Artifact;
import com.example.scavengerhunt.Entities.Scavenger;
import com.example.scavengerhunt.Entities.Session;
import com.example.scavengerhunt.Entities.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Database {

    private static Database INSTANCE = new Database();

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Session session = Session.getInstance();
    public String test = "test";
    public enum Action {
        START,
        PAUSE,
        END,
        ARTIFACT_COLLECTED,
        NEW_MESSAGE,
        NEW_STORY
    }
    //private User user = User.getInstance();


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

    public void joinSession(String sessionId, Scavenger scavenger) {
        //TODO make sure session exists
        User.getInstance().setActiveSessionId(sessionId);
        //mDatabase.child("active_sessions").child(sessionId).child("scavengers").child(scavenger.getUser().getId()).setValue(scavenger);
        mDatabase.child("active_sessions").child(sessionId).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            }
            else {
                session = task.getResult().getValue(Session.class);
                session.addScavenger(Scavenger.getInstance());
                //user.setActiveSessionId(sessionId);

                updateSession();
                //--------------------------------------------------------------
                Log.d("SC", "Session ID: " + session.getSessionId());
                for (Scavenger s: session.getScavengers() ){
                    Log.d("SC", "Scavengers: " + s.getUser().getName() + " role: " + s.getRole());
                }
                //--------------------------------------------------------------
            }
        });
    }

    public void updateSession() {
        mDatabase.child("active_sessions").child(
                User.getInstance().getActiveSessionId()).setValue(session);

        Log.d("SC", "UPDATE: Session ID: " + session.getSessionId());
        for (Scavenger s: session.getScavengers() ){
            Log.d("SC", "UPDATE: Scavengers: " + s.getUser().getName() + " role: " + s.getRole());
        }
    }

    public void updateRole(String sessionId, int scavengerId, String role) {
        mDatabase.child("active_sessions").child(sessionId).child("scavengers")
                .child(String.valueOf(Scavenger.getInstance().getScavengerId())).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            }
            else {
                Log.d("firebase", "Scavenger ID: " + task.getResult().getValue().toString());
            }
        });

        mDatabase.child("active_sessions").child(sessionId).child("scavengers")
                .child(String.valueOf(Scavenger.getInstance().getScavengerId())).child("role").setValue(role);

    }

    public Session replaceInstace() {
        return session;
    }

    public void addLog() {
        Log.d("ADDED", "add logs called");

        mDatabase.child("active_sessions").child(
                User.getInstance().getActiveSessionId()).child("log").setValue(Session.getInstance().logs);

    }

    public void newAction(Action action) {
        String sessionId = User.getInstance().getActiveSessionId();
        switch (action) {
            case START:
                String sAction = mDatabase.child("active_sessions").child(sessionId).child("actions").push().getKey();
                mDatabase.child("active_sessions").child(sessionId).child("actions").child(sAction).setValue("start");
        }
    }

    public void testLogArtifact() {

        mDatabase.child("artifacts").child("shield").get().addOnCompleteListener(task -> {
           if(task.isSuccessful()){
               Artifact artifact = task.getResult().getValue(Artifact.class);

               com.example.scavengerhunt.Entities.Log log = new com.example.scavengerhunt.Entities.
                       Log("artifact", artifact.getName(), "Description:",
                       "12:23 - Artifact Collected", artifact.getDescription(), artifact);

               Session.getInstance().addLog(log);
               addLog();

               /*
               String key = mDatabase.child("active_sessions").child(User.getInstance()
                       .getActiveSessionId()).child("logs").push().getKey();

               mDatabase.child("active_sessions").child(User.getInstance().getActiveSessionId()).
                       child("logs").child(key).setValue(log);*/

           }


        });
    }



}

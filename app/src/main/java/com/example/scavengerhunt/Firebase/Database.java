package com.example.scavengerhunt.Firebase;

import android.util.Log;

import com.example.scavengerhunt.Entities.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Database {

    private DatabaseReference mDatabase;

    public Database() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void writeNewUser(User user) {
        mDatabase.child("users").child(user.getId()).setValue(user);
        Log.d("DB", "ID: " + user.getId() + " email: " + user.getEmail());
    }


}

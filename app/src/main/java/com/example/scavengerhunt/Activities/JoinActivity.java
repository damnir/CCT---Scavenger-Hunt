package com.example.scavengerhunt.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.scavengerhunt.Entities.Scavenger;
import com.example.scavengerhunt.Entities.User;
import com.example.scavengerhunt.Firebase.Database;
import com.example.scavengerhunt.R;

public class JoinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        Scavenger scavenger = new Scavenger(User.getInstance(), "123815");
        Database dbRef = Database.getInstance();

        dbRef.joinSession("254725", scavenger);
    }


}
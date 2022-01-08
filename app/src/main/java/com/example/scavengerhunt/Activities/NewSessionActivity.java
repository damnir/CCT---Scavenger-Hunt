package com.example.scavengerhunt.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scavengerhunt.Entities.Session;
import com.example.scavengerhunt.Entities.User;
import com.example.scavengerhunt.R;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Random;
import java.util.stream.IntStream;

public class NewSessionActivity extends AppCompatActivity {

    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_session);

        session = new Session(generateSessionId());
    }


    public String generateSessionId() {
        Random rand = new Random();
        String id = "";
        for(int i = 0; i < 6; i++) id += rand.nextInt(9);
        return id;
    }


}
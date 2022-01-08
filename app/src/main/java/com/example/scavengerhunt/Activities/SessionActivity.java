package com.example.scavengerhunt.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.example.scavengerhunt.Entities.User;
import com.example.scavengerhunt.R;

import java.util.concurrent.TimeUnit;

public class SessionActivity extends AppCompatActivity {

    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        Log.d("name", "Name on create: " + User.getInstance().getName());

        name = findViewById(R.id.sessionUsername);
        name.setText(User.getInstance().getName());
    }



}
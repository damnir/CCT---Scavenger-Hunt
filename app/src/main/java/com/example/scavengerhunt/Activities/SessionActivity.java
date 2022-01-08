package com.example.scavengerhunt.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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

    public void newSessionClick(View v) {
        Intent intent = new Intent(this, NewSessionActivity.class);
        startActivity(intent);
    }

    public void joinSessionClick(View v) {
        Intent intent = new Intent(this, JoinActivity.class);
        startActivity(intent);
    }



}
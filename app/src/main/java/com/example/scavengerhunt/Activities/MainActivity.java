package com.example.scavengerhunt.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.scavengerhunt.R;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //request location permissions
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        //Intent intent = new Intent(this, CompassActivity.class);
        //startActivity(intent);

    }

    public void onBeginClick(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }

    public void onARClick(View v) {
        Intent intent = new Intent(this, AugmentedRealityActivity.class);
        startActivity(intent);
    }


}
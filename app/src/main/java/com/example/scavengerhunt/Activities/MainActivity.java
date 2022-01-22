package com.example.scavengerhunt.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.scavengerhunt.R;

public class MainActivity extends AppCompatActivity {

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
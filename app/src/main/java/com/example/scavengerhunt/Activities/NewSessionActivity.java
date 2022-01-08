package com.example.scavengerhunt.Activities;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scavengerhunt.Entities.User;
import com.example.scavengerhunt.R;
import android.os.Bundle;
import android.widget.TextView;

public class NewSessionActivity extends AppCompatActivity {

    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_session);

        name = findViewById(R.id.sessionUsername);
        name.setText(User.getInstance().getName());
    }


}
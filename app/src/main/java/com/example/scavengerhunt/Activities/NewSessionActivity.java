package com.example.scavengerhunt.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.scavengerhunt.Entities.Scavenger;
import com.example.scavengerhunt.Entities.Session;
import com.example.scavengerhunt.Entities.User;
import com.example.scavengerhunt.Firebase.Database;
import com.example.scavengerhunt.R;
import com.example.scavengerhunt.ViewModels.SessionViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.annotations.Nullable;

import android.os.Bundle;
import android.util.Log;

import java.util.Random;

public class NewSessionActivity extends AppCompatActivity {

    private Session session;
    private Database dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_session);

        dbRef = Database.getInstance();

        String newSessionId = generateSessionId();
        User.getInstance().setActiveSessionId(newSessionId);
        session = Session.getInstance();
        session.setSessionId(newSessionId);
        session.setOwner(User.getInstance());
        session.addScavenger(new Scavenger(User.getInstance()));

        dbRef.newSession(session);

        SessionViewModel viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SessionViewModel.class);


        LiveData<DataSnapshot> liveData = viewModel.getUsersLiveDataSS();

        liveData.observe(this, dataSnapshot -> {
            if (dataSnapshot != null) {
                //Log.d("ONCHANGED: ", String.valueOf(dataSnapshot));
                //viewModel.formatSession(dataSnapshot);
                session =  dataSnapshot.getValue(Session.class);

                Log.d("SC", "Session ID: " + session.getSessionId());
                for (Scavenger s: session.getScavengers() ){
                    Log.d("SC", "Scavengers: " + s.getUser().getName() + " role: " + s.getRole());
                }
            }
        });
    }


    public String generateSessionId() {
        Random rand = new Random();
        String id = "";
        for(int i = 0; i < 6; i++) id += rand.nextInt(9);
        return id;
    }


}
package com.example.scavengerhunt.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scavengerhunt.Entities.Scavenger;
import com.example.scavengerhunt.Entities.Session;
import com.example.scavengerhunt.Entities.User;
import com.example.scavengerhunt.Firebase.Database;
import com.example.scavengerhunt.R;
import com.example.scavengerhunt.SessionAdapter;
import com.example.scavengerhunt.ViewModels.SessionViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.annotations.Nullable;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Random;

public class NewSessionActivity extends AppCompatActivity {

    private Session session;
    private Database dbRef;
    private SessionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_session);

        dbRef = Database.getInstance();


        session = Session.getInstance();
        Log.d("SC", "Session ID 222: " + session.getSessionId());
        Log.d("SC", "Session ID 222USER: " + User.getInstance().getActiveSessionId());



        adapter = new SessionAdapter(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SessionViewModel viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SessionViewModel.class);

        viewModel.updateDBReference();

        LiveData<DataSnapshot> liveData = viewModel.getUsersLiveDataSS();

        liveData.observe(this, dataSnapshot -> {
            if (dataSnapshot != null) {
                //Log.d("ONCHANGED: ", String.valueOf(dataSnapshot));
                //viewModel.formatSession(dataSnapshot);

                    Log.d("SC", "Session ID BEFORE NULL: " + session.getSessionId());

                    Log.d("SC", "DATASNAPSHOT!?!?!?: " + dataSnapshot.toString() + "KEY:" + dataSnapshot.getKey());
                    session =  dataSnapshot.getValue(Session.class);
                    adapter.setData(session.getScavengers());

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

    public void onNavigatorClick(View v) {
        session.updateRole(Scavenger.getInstance().getScavengerId(), "Navigator");
    }

    public void onDiscovererClick(View v) {
        session.updateRole(Scavenger.getInstance().getScavengerId(), "Discoverer");
    }

    public void onStoryClick(View v) {
        session.updateRole(Scavenger.getInstance().getScavengerId(), "Story Teller");
    }


}
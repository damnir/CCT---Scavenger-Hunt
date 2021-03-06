package com.example.scavengerhunt.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scavengerhunt.Entities.Scavenger;
import com.example.scavengerhunt.Entities.Session;
import com.example.scavengerhunt.Firebase.Database;
import com.example.scavengerhunt.R;
import com.example.scavengerhunt.Misc.Adapters.SessionAdapter;
import com.example.scavengerhunt.ViewModels.SessionViewModel;
import com.google.firebase.database.DataSnapshot;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class NewSessionActivity extends AppCompatActivity {

    private Session session;
    private Database dbRef;
    private SessionAdapter adapter;

    TextView sessionIdText;
    SessionViewModel viewModel;

    private LiveData<DataSnapshot> actionLiveData;
    LiveData<DataSnapshot> liveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_session);

        session = Session.getInstance();

        dbRef = Database.getInstance();
        session = Session.getInstance();

        sessionIdText = findViewById(R.id.newSessId);

        adapter = new SessionAdapter(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SessionViewModel.class);

        viewModel.updateDBReference();

        liveData = viewModel.getUsersLiveDataSS();
        actionLiveData = viewModel.getAction();

        liveData.observe(this, dataSnapshot -> {
            if (dataSnapshot != null) {
                try {
                    session =  dataSnapshot.getValue(Session.class);
                    adapter.setData(session.getScavengers());
                    sessionIdText.setText(session.getSessionId());
                }catch (NullPointerException ignored) {}
            }
        });


        actionLiveData.observe(this, dataSnapshot -> {
            if (dataSnapshot != null) {
                String latestAction = "";

                try {
                    for (DataSnapshot c:dataSnapshot.getChildren()) {
                        latestAction = c.getValue(String.class);
                    }
                    if (latestAction.equals("start")) {
                        Log.d("LIVE", "YESdatasnapshot: " + dataSnapshot);

                        liveData.removeObservers(this);
                        actionLiveData.removeObservers(this);

                        Intent intent = new Intent(this, HuntActivity.class);
                        startActivity(intent);
                    }
                }catch (NullPointerException ignored){}

            }
        });
    }

    public void onNavigatorClick(View v) {
        Scavenger.getInstance().setRole("Navigator");
        session.updateRole(Scavenger.getInstance().getScavengerId(), "Navigator");
    }

    public void onDiscovererClick(View v) {
        Scavenger.getInstance().setRole("Discoverer");
        session.updateRole(Scavenger.getInstance().getScavengerId(), "Discoverer");
    }

    public void onStoryClick(View v) {
        Scavenger.getInstance().setRole("Story Teller");
        session.updateRole(Scavenger.getInstance().getScavengerId(), "Story Teller");
    }

    public void onStartClick(View v) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast toast=Toast.makeText(getApplicationContext(),"Allow location permissions for this feature to work",Toast.LENGTH_LONG);
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            toast.show();
            return;
        }
        dbRef.startAction(Database.Action.START);

        liveData.removeObservers(this);
        actionLiveData.removeObservers(this);

        Intent intent = new Intent(this, HuntActivity.class);
        startActivity(intent);
    }


}
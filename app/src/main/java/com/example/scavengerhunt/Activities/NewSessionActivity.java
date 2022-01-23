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
import com.example.scavengerhunt.Misc.SessionAdapter;
import com.example.scavengerhunt.ViewModels.SessionViewModel;
import com.google.firebase.database.DataSnapshot;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class NewSessionActivity extends AppCompatActivity {

    private Session session;
    private Database dbRef;
    private SessionAdapter adapter;

    TextView sessionIdText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_session);

        dbRef = Database.getInstance();
        session = Session.getInstance();

        sessionIdText = findViewById(R.id.newSessId);

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
                    session =  dataSnapshot.getValue(Session.class);
                    adapter.setData(session.getScavengers());
                    sessionIdText.setText(session.getSessionId());
            }
        });
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

    public void onStartClick(View v) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast toast=Toast.makeText(getApplicationContext(),"Allow location permissions for this feature to work",Toast.LENGTH_LONG);
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            toast.show();
            return;
        }
        Intent intent = new Intent(this, HuntActivity.class);
        startActivity(intent);
    }


}
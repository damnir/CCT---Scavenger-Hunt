package com.example.scavengerhunt.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.scavengerhunt.Entities.Action;
import com.example.scavengerhunt.Entities.Artifact;
import com.example.scavengerhunt.Entities.Session;
import com.example.scavengerhunt.Entities.Site;
import com.example.scavengerhunt.Entities.Story;
import com.example.scavengerhunt.Entities.User;
import com.example.scavengerhunt.Firebase.Database;
import com.example.scavengerhunt.Misc.Adapters.CompactArtifactAdapter;
import com.example.scavengerhunt.Misc.Adapters.CompactSiteAdapter;
import com.example.scavengerhunt.Misc.Adapters.LogAdapter;
import com.example.scavengerhunt.Misc.Adapters.StoryAdapter;
import com.example.scavengerhunt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;


public class EndActivity extends AppCompatActivity {

    private CompactArtifactAdapter artifactAdapter;
    private CompactSiteAdapter siteAdapter;

    private Session session;
    String timeS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        session = Session.getInstance();

        artifactAdapter = new CompactArtifactAdapter(this);
        siteAdapter = new CompactSiteAdapter(this);

        RecyclerView recyclerArtifact = findViewById(R.id.artifact_recycler);
        recyclerArtifact.setAdapter(artifactAdapter);
        recyclerArtifact.setLayoutManager(new LinearLayoutManager(this));

        List<Artifact> artifactList = new ArrayList<>();

        Database.getInstance().getDBRef().child("active_sessions").child(User.getInstance().getActiveSessionId())
                .child("artifacts").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for(DataSnapshot c : task.getResult().getChildren()) {
                    artifactList.add(c.getValue(Artifact.class));
                }
                artifactAdapter.setData(artifactList);
            }
        });

        RecyclerView reyclerSite = findViewById(R.id.site_recycler);
        reyclerSite.setAdapter(siteAdapter);
        reyclerSite.setLayoutManager(new LinearLayoutManager(this));

        List<Site> siteList = new ArrayList<>();

        Database.getInstance().getDBRef().child("active_sessions").child(User.getInstance().getActiveSessionId())
                .child("sites").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for(DataSnapshot c : task.getResult().getChildren()) {
                    siteList.add(c.getValue(Site.class));
                }
                siteAdapter.setData(siteList);
            }
        });


        TextView sessionId = findViewById(R.id.end_sessionID);
        TextView time = findViewById(R.id.end_time);

        sessionId.setText(User.getInstance().getActiveSessionId());
        Database.getInstance().getDBRef().child("active_sessions").child(User.getInstance().getActiveSessionId())
                .child("time").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                timeS = task.getResult().getValue(String.class);
                time.setText(timeS);
            }
        });


    }

    public void popupLog(View v) {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.genereic_recycler, null);
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(findViewById(android.R.id.content).getRootView()
                , Gravity.CENTER, 0, 0);

        LogAdapter adapter = new LogAdapter(this);
        RecyclerView recyclerView = popupView.findViewById(R.id.recycler_generic);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<com.example.scavengerhunt.Entities.Log> logs = new ArrayList<>();

        Database.getInstance().getDBRef().child("active_sessions")
                .child(User.getInstance().getActiveSessionId()).child("log").get().addOnCompleteListener(
                new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        for(DataSnapshot c : task.getResult().getChildren()){
                            logs.add(c.getValue(com.example.scavengerhunt.Entities.Log.class));
                        }
                        adapter.setData(logs);
                    }
                }
        );


        // dismiss the popup window when touched
        FloatingActionButton button = popupView.findViewById(R.id.generic_fob);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public void popupStory(View v) {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.genereic_recycler, null);
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(findViewById(android.R.id.content).getRootView()
                , Gravity.CENTER, 0, 0);

        StoryAdapter adapter = new StoryAdapter(this);
        RecyclerView recyclerView = popupView.findViewById(R.id.recycler_generic);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Story> storyList = new ArrayList<>();

        Database.getInstance().getDBRef().child("active_sessions")
                .child(User.getInstance().getActiveSessionId()).child("story").get().addOnCompleteListener(
                new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        for(DataSnapshot c : task.getResult().getChildren()){
                            storyList.add(c.getValue(Story.class));
                        }
                        adapter.setData(storyList);
                    }
                }
        );


        // dismiss the popup window when touched
        FloatingActionButton button = popupView.findViewById(R.id.generic_fob);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public void end (View v) {
        finish();
    }
}
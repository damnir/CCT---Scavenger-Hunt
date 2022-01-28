package com.example.scavengerhunt.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.scavengerhunt.Activities.HuntFragments.ARFragment;
import com.example.scavengerhunt.Activities.HuntFragments.CompassFragment;
import com.example.scavengerhunt.Activities.HuntFragments.LogFragment;
import com.example.scavengerhunt.Activities.HuntFragments.MapFragment;
import com.example.scavengerhunt.Activities.HuntFragments.RadarFragment;
import com.example.scavengerhunt.Activities.HuntFragments.StoryFragment;
import com.example.scavengerhunt.Entities.Action;
import com.example.scavengerhunt.Entities.Notification;
import com.example.scavengerhunt.Entities.Scavenger;
import com.example.scavengerhunt.Entities.Session;
import com.example.scavengerhunt.Entities.Site;
import com.example.scavengerhunt.Entities.User;
import com.example.scavengerhunt.Firebase.Database;
import com.example.scavengerhunt.Misc.CurrentTime;
import com.example.scavengerhunt.Misc.GeofenceBroadcastReceiver;
import com.example.scavengerhunt.Misc.HuntPagerAdapter;
import com.example.scavengerhunt.R;
import com.example.scavengerhunt.Misc.TrackingService;
import com.example.scavengerhunt.ViewModels.ActionViewModel;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class HuntActivity extends AppCompatActivity {

    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    private MapFragment mapFragment;
    private TrackingService trackingService;

    private Database dbRef = Database.getInstance();

    private List<Geofence> geofenceList;
    private GeofencingClient geofencingClient;

    private PendingIntent geofencePendingIntent;

    private Session session;

    private TextView nameText;
    private TextView roleText;
    private TextView sessionText;
    private TextView timeText;

    private CurrentTime currentTime;

    private List<Fragment> fragmentList;
    private TabLayout tabLayout;
    LiveData<DataSnapshot> liveData;

    private static final String CHANNEL_ID = "HUNTACT";
    private NotificationManager notificationManager;

    private Action action;

    ActionViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hunt);

        currentTime = new CurrentTime();

        createNotificationChannel();

        session = Session.getInstance();
        nameText = findViewById(R.id.navigate_name);
        roleText = findViewById(R.id.navigate_role);
        sessionText = findViewById(R.id.end_sessionID);
        timeText = findViewById(R.id.end_time);
        updateText();

        fragmentList = new ArrayList<>();
        setupFragments(Scavenger.getInstance().getRole());

        pager = findViewById(R.id.pager);
        pagerAdapter = new HuntPagerAdapter(getSupportFragmentManager(), fragmentList);
        pager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager);

        //tabLayout.getTabAt(0).setIcon(R.drawable.camping_icon_13511);

        geofencingClient = LocationServices.getGeofencingClient(this);
        geofenceList = new ArrayList<>();

        String requestId1 = "1";
        String requestId2 = "2";
        String requestId3 = "3";


        Site site1 = new Site("UP Intersection", "Some random text",
                "https://firebasestorage.googleapis.com/v0/b/cct-scavenger-hunt.appspot.com/o/lakes" +
                        "idearts.png?alt=media&token=f0d00034-74f2-4a0b-91dc-d3e6aa0dca89",
                false, 52.937945, -1.189676);

        Site site2 = new Site("Highfield Park", "Some random text", "https://firebasestorage.google" +
                "apis.com/v0/b/cct-scavenger-hunt.appspot.com/o/highfield.png?alt=media&token=2030211" +
                "1-6eb0-450c-8521-4986d19b75c9", false, 52.935587, -1.194325);

        Site site3 = new Site("Highfield Park", "Some random text", "https://firebasestorage.google" +
                "apis.com/v0/b/cct-scavenger-hunt.appspot.com/o/highfield.png?alt=media&token=2030211" +
                "1-6eb0-450c-8521-4986d19b75c9", true, 52.933083, -1.201410);

        addGeofence(requestId1, site1.getLat(), site1.getLng(), 80);
        addGeofence(requestId2, site2.getLat(), site2.getLng(), 80);
        addGeofence(requestId3, site3.getLat(), site3.getLng(), 80);

        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(this, aVoid -> {
                    android.util.Log.d("GEO", "geofence added");
                })
                .addOnFailureListener(this, e -> {
                    android.util.Log.d("GEO", "geofence failed to add");
                });


        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(ActionViewModel.class);

        viewModel.updateDBReference();

        liveData = viewModel.getActionsLiveData();

        Database.getInstance().getDBRef().child("active_sessions").child(User.getInstance().getActiveSessionId()).child("actions").removeValue();


        liveData.observe(this, dataSnapshot -> {
            if (dataSnapshot != null) {
                if(dataSnapshot.hasChildren()) {
                    for(DataSnapshot c : dataSnapshot.getChildren()) {
                        action =  c.getValue(Action.class);
                        //c.getRef().removeValue();
                    }
                    try {
                        if(Session.getInstance().getOwner().getName().equals(Scavenger.getInstance().getUser().getName()))
                        {
                            dataSnapshot.getRef().removeValue();
                        }
                    }catch (NullPointerException ignored){}

                    manageAction(action);
                }
            }
        });


        Intent intent = new Intent(this, TrackingService.class);
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        progressHandler.post(updateProgress);
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        geofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        //on service connected bind to the service and getService
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TrackingService.MyBinder myBinder = (TrackingService.MyBinder) service;
            trackingService = myBinder.getService();
            trackingService.setStatus("TRACKING"); //start tracking

        }
        //on disconnected null the service reference
        @Override
        public void onServiceDisconnected(ComponentName name) {
            trackingService = null;
        }
    };

    private void addGeofence(String id, Double lat, Double lng, int radius) {
        geofenceList.add(new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId(id)

                .setCircularRegion(
                        lat,
                        lng,
                        radius
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());
    }

    private void updateText() {
        nameText.setText(User.getInstance().getName());
        roleText.setText(Scavenger.getInstance().getRole());
        sessionText.setText(User.getInstance().getActiveSessionId());
    }


    public void onARButtonClick(View v) {
        Intent intent = new Intent(this, AugmentedRealityActivity.class);
        startActivity(intent);
    }


    public void onNewStoryClick(View v) {
        Intent intent = new Intent(this, NewStoryActivity.class);
        startActivity(intent);
    }

    private void setupFragments(String role) {

        switch (role) {
            case "Navigator" :
                fragmentList.add(new CompassFragment());
                fragmentList.add(new MapFragment());
                fragmentList.add(new LogFragment());
                break;
            case "Discoverer" :
                fragmentList.add(new RadarFragment());
                fragmentList.add(new MapFragment());
                fragmentList.add(new LogFragment());
                fragmentList.add(new ARFragment());
                break;
            case "Story Teller" :
                fragmentList.add(new StoryFragment());
                fragmentList.add(new MapFragment());
                fragmentList.add(new LogFragment());
        }

    }

    public void manageAction(Action action) {
        switch(action.getType()) {
            case "artifact" :
                notificationManager.notify(0, Notification.getInstance().
                        newArtifactNotification(this, CHANNEL_ID, action.getData1()));
                break;

            case "site" :
                notificationManager.notify(0, Notification.getInstance().
                        newSiteNotification(this, CHANNEL_ID));
                break;

            case "message" :
                if(action.getData1().contains(User.getInstance().getName())) break;
                notificationManager.notify(0, Notification.getInstance().
                        newMessageNotification(this, CHANNEL_ID,
                                action.getData1(), action.getData2()));
                break;

            case "story" :
                if(Scavenger.getInstance().getRole().equals("Story Teller")) break;
                notificationManager.notify(0, Notification.getInstance().
                        newStoryNotification(this, CHANNEL_ID));
                break;

            case "final" :
                Database.getInstance().getDBRef().child("active_sessions")
                        .child(User.getInstance().getActiveSessionId()).child("time").setValue(currentTime.getElapsed()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        end();
                    }
                });

        }
    }

    private void end() {
        Intent intent = new Intent(this, EndActivity.class);
        startActivity(intent);
        finish();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notify";
            String description = "notifyDescription";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = this.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private Handler progressHandler = new Handler();
    //runnable, updated every second to give live UI updates
    private Runnable updateProgress = new Runnable() {
        @Override
        public void run() {

            timeText.setText(currentTime.getElapsed());

            //post a 1 second delay before updating again
            progressHandler.postDelayed(this, 1000);

        }
    };







}
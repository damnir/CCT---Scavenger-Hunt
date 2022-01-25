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
import com.example.scavengerhunt.Entities.Log;
import com.example.scavengerhunt.Entities.Session;
import com.example.scavengerhunt.Firebase.Database;
import com.example.scavengerhunt.Misc.GeofenceBroadcastReceiver;
import com.example.scavengerhunt.Misc.HuntPagerAdapter;
import com.example.scavengerhunt.R;
import com.example.scavengerhunt.Misc.TrackingService;
import com.example.scavengerhunt.ViewModels.SessionViewModel;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class HuntActivity extends AppCompatActivity {

    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    private MapFragment mapFragment;
    private GoogleMap map;

    private TrackingService trackingService;

    private Database dbRef = Database.getInstance();

    private List<Geofence> geofenceList;
    private GeofencingClient geofencingClient;

    private PendingIntent geofencePendingIntent;

    private Session session;

    LiveData<DataSnapshot> liveData;

    SessionViewModel viewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hunt);

        session = Session.getInstance();

        List<Fragment> fragmentList = new ArrayList<>();

        mapFragment = new MapFragment();

        fragmentList.add(new CompassFragment());
        fragmentList.add(mapFragment);
        fragmentList.add(new LogFragment());
        fragmentList.add(new RadarFragment());
        fragmentList.add(new ARFragment());

        pager = findViewById(R.id.pager);
        pagerAdapter = new HuntPagerAdapter(getSupportFragmentManager(), fragmentList);
        pager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager);

        tabLayout.getTabAt(0).setIcon(R.drawable.camping_icon_13511);

        geofencingClient = LocationServices.getGeofencingClient(this);
        geofenceList = new ArrayList<>();

        String requestId1 = "1";
        String requestId2 = "2";

        geofenceList.add(new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId(requestId1)

                .setCircularRegion(
                        52.937945,
                        -1.189676,
                        75
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());

        geofenceList.add(new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId(requestId2)

                .setCircularRegion(
                        52.935587,
                        -1.194325,
                        75
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());



        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(this, aVoid -> {
                    android.util.Log.d("GEO", "geofence added");
                    // Geofences added
                    // ...
                })
                .addOnFailureListener(this, e -> {
                    // Failed to add geofences
                    // ...
                    android.util.Log.d("GEO", "geofence failed to add");

                });

        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SessionViewModel.class);

        viewModel.updateDBReference();

        liveData = viewModel.getUsersLiveDataSS();

        liveData.observe(this, dataSnapshot -> {
            if (dataSnapshot != null) {
                if(dataSnapshot.hasChildren()) {
                    session =  dataSnapshot.getValue(Session.class);
                }

            }
        });


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

    //TEST
    public Log addLogTest() {
        Log log = new Log();
        log.setStamp("13:43 Artifact Collected");
        log.setTitle("Some Rock");
        log.setLabel("Found at:");
        log.setDescription("lksdnfglk;dsnf sdlkf nsdlkf jsdlkf jsdlkfdsfsdljk  jklsdf");

        return log;
    }

    public void onARButtonClick(View v) {
        Intent intent = new Intent(this, AugmentedRealityActivity.class);
        startActivity(intent);
    }


}
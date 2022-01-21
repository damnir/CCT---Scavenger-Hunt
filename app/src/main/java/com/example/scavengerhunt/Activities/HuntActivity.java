package com.example.scavengerhunt.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.scavengerhunt.Activities.HuntFragments.CompassFragment;
import com.example.scavengerhunt.Activities.HuntFragments.LogFragment;
import com.example.scavengerhunt.Activities.HuntFragments.MapFragment;
import com.example.scavengerhunt.Activities.HuntFragments.RadarFragment;
import com.example.scavengerhunt.HuntPagerAdapter;
import com.example.scavengerhunt.R;
import com.example.scavengerhunt.TrackingService;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.tabs.TabLayout;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class HuntActivity extends AppCompatActivity {

    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    private MapFragment mapFragment;
    private GoogleMap map;

    private TrackingService trackingService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hunt);

        List<Fragment> fragmentList = new ArrayList<>();

        mapFragment = new MapFragment();

        fragmentList.add(new CompassFragment());
        fragmentList.add(mapFragment);
        fragmentList.add(new LogFragment());
        fragmentList.add(new RadarFragment());

        pager = findViewById(R.id.pager);
        pagerAdapter = new HuntPagerAdapter(getSupportFragmentManager(), fragmentList);

        pager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager);

        /*
        Intent intent = new Intent(this, TrackingService.class);
        startService(intent);
        this.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        */
    }

    /*
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.map = googleMap;
    }*/


    /*
    private ServiceConnection serviceConnection = new ServiceConnection() {
        //on service connected bind to the service and getService
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TrackingService.MyBinder myBinder = (TrackingService.MyBinder) service;
            trackingService = myBinder.getService();
            trackingService.setStatus("TRACKING"); //start tracking
            //progressHandler.postDelayed(updateProgress, 0); //update UI based on progress
            //update(); //call update to update all UI elements
        }
        //on disconnected null the service reference
        @Override
        public void onServiceDisconnected(ComponentName name) {
            trackingService = null;
        }
    };*/


}
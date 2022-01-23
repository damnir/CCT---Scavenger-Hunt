package com.example.scavengerhunt.Activities.HuntFragments;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scavengerhunt.R;
import com.example.scavengerhunt.Misc.TrackingService;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment implements OnMapReadyCallback{

    public MapFragment() {
        // Required empty public constructor
    }

    private Location location;
    private TrackingService trackingService;
    private Activity activity;
    private GoogleMap map;
    private BitmapDescriptor smallMarkerIcon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();

        Intent intent = new Intent(getActivity(), TrackingService.class);
        activity.startService(intent);
        activity.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("check","onCreateView");
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_map, container, false);
        // Gets the MapView from the XML layout and creates it
        final SupportMapFragment myMAPF = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        myMAPF.getMapAsync(this);
        return  view;
    }

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
    };


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        Log.d("MAP", "MAP READY");

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable. user);
        Bitmap smallMarker = Bitmap.createScaledBitmap(icon, 75, 75, false);
        smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);

        progressHandler.postDelayed(updateProgress, 0); //update UI based on progress
    }

    private Handler progressHandler = new Handler();
    //runnable, updated every second to give live UI updates
    private Runnable updateProgress = new Runnable() {
        @Override
        public void run() {
            location = trackingService.getLastLocation();
            double lat = Math.round(location.getLatitude() * 1000.000)/1000.00;;
            double lng = Math.round(location.getLongitude() * 1000.000)/1000.00;
            LatLng pos = new LatLng(lat, lng);
            map.clear();


            Log.d("MAP", "LatLng" + lat + lng);
            map.addMarker(new MarkerOptions()
                    .position(pos)
                    .title("Me")
                    .icon(smallMarkerIcon));

            map.addCircle(new CircleOptions()
                    .center(pos)
                    .radius(50)
                    .strokeColor(Color.RED)
                    .fillColor(Color.BLUE));


            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(pos, 16);
            map.moveCamera(cameraUpdate);

            //post a 1 second delay before updating again
            progressHandler.postDelayed(this, 1000);

        }
    };
}
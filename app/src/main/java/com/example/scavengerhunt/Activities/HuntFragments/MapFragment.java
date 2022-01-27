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
import android.location.LocationManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.scavengerhunt.Entities.Scavenger;
import com.example.scavengerhunt.Entities.Session;
import com.example.scavengerhunt.Firebase.Database;
import com.example.scavengerhunt.Misc.LatLngConverter;
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
    private TextView coords;
    private LatLngConverter latLngConverter;
    private TextView distance;
    private TextView finalDestText;
    private TextView time;


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

        latLngConverter = new LatLngConverter();
        //TEMPORARY VARIABLE - Reuse destination site object to get coordinates pls

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
            if(location == null) return;
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            /*
            Scavenger.getInstance().setaLat(lat);
            Scavenger.getInstance().setLng(lng);
            Session.getInstance().updateScavenger();
            Database.getInstance().updateSession();*/
            Database.getInstance().updateLocation(lat, lng);

            //LatLng pos = new LatLng(lat, lng);
            map.clear();
            LatLng pos = new LatLng(lat, lng);
            for(Scavenger c : Database.getInstance().getAllScavengers()) {
                pos = new LatLng(c.getaLat(), c.getLng());
                map.addMarker(new MarkerOptions()
                        .position(pos)
                        .title(c.getUser().getName())
                        .icon(smallMarkerIcon));
            }


            Log.d("MAP", "LatLng" + lat + lng);



            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(pos, 16);
            map.moveCamera(cameraUpdate);

            String covertedCoords = latLngConverter.latnglToDMS((float)location.getLatitude(),
                    (float)location.getLongitude());

            coords = getActivity().findViewById(R.id.navigate_coords);
            distance = getActivity().findViewById(R.id.navigate_distance);
            finalDestText = getActivity().findViewById(R.id.navigate_final);
            time = getActivity().findViewById(R.id.navigate_time);

            try {
                coords.setText(covertedCoords);
                distance.setText(distance(location, 52.935587, -1.194325));
                time.setText(trackingService.getTime());
                finalDestText.setText(latLngConverter.latnglToDMS((float)52.935587, (float)-1.194325));

            }catch (NullPointerException ignored){}

            //post a 1 second delay before updating again
            progressHandler.postDelayed(this, 3500);

        }
    };

    private String distance(Location location, Double destinationLat, Double destinationLng) {
        Location destination = new Location("dest");
        destination.setLatitude(destinationLat);
        destination.setLongitude(destinationLng);
        float distance = location.distanceTo(destination);
        if(distance < 1000) {
            return ((int)distance + "m away");
        }
        else{
            return (distance/1000 + "km away");
        }
    }
}
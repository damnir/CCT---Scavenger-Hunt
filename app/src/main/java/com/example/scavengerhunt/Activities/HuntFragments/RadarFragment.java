package com.example.scavengerhunt.Activities.HuntFragments;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scavengerhunt.Entities.Artifact;
import com.example.scavengerhunt.Misc.DataManager;
import com.example.scavengerhunt.Misc.TrackingService;
import com.example.scavengerhunt.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RadarFragment#} factory method to
 * create an instance of this fragment.
 */
public class RadarFragment extends Fragment implements OnMapReadyCallback {


    private GoogleMap map;
    private Activity activity;

    //private FusedLocationProviderClient fusedLocationClient;
    private BitmapDescriptor smallMarkerIcon;

    private TrackingService trackingService;

    private Location location;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("check","onCreateView");
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_radar, container, false);
        // Gets the MapView from the XML layout and creates it
        final SupportMapFragment myMAPF = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map2);
        myMAPF.getMapAsync(this);
        return  view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        try {
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                    activity, R.raw.style_json));

            if (!success) {
                Log.e("TAG", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("TAG", "Can't find style. Error: ", e);
        }
        //fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable. user);
        Bitmap smallMarker = Bitmap.createScaledBitmap(icon, 75, 75, false);
        smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
        this.map = googleMap;

        Intent intent = new Intent(getActivity(), TrackingService.class);
        activity.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        activity.startService(intent);

    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        //on service connected bind to the service and getService
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TrackingService.MyBinder myBinder = (TrackingService.MyBinder) service;
            trackingService = myBinder.getService();
            trackingService.setStatus("TRACKING"); //start tracking
            progressHandler.postDelayed(updateProgress, 0); //update UI based on progress

            //progressHandler.postDelayed(updateProgress, 0); //update UI based on progress
            //update(); //call update to update all UI elements
        }
        //on disconnected null the service reference
        @Override
        public void onServiceDisconnected(ComponentName name) {
            trackingService = null;
        }
    };

    private Handler progressHandler = new Handler();
    //runnable, updated every second to give live UI updates
    private Runnable updateProgress = new Runnable() {
        @Override
        public void run() {

            /*
            fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                double lat = Math.round(location.getLatitude() * 1000.000)/1000.00;;
                                double lng = Math.round(location.getLongitude() * 1000.000)/1000.00;
                                LatLng pos = new LatLng(lat, lng);
                                map.clear();


                                Log.d("MAP", "LatLng" + lat + lng);
                                map.addMarker(new MarkerOptions()
                                        .position(pos)
                                        .title("Me")
                                        .icon(smallMarkerIcon));

                                int area = ManualData.getInstance().inArea();

                                if(area != 0) {
                                    Log.d("GEO", "adding artifact, ID: " + area);
                                    Artifact artifact = ManualData.getInstance().artifactsList.get(area-1);
                                    LatLng latLng = new LatLng(artifact.getAlt(), artifact.getLng());
                                    map.addMarker(new MarkerOptions()
                                    .position(latLng));
                                }

                                map.addCircle(new CircleOptions()
                                        .center(pos)
                                        .radius(75)
                                        .strokeColor(Color.RED)
                                        .fillColor(0x7FDD0000));

                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(pos)
                                        .zoom(18)
                                        .tilt(67.5f)
                                        .bearing(314)
                                        .build();


                                map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));



                            }
                        }
                    });*/



            location = trackingService.getLastLocation();
            if (location != null) {
                double lat = Math.round(location.getLatitude() * 1000.000) / 1000.00;

                double lng = Math.round(location.getLongitude() * 1000.000) / 1000.00;
                LatLng pos = new LatLng(lat, lng);
                map.clear();


                Log.d("MAP", "LatLng" + lat + lng);
                map.addMarker(new MarkerOptions()
                        .position(pos)
                        .title("Me")
                        .icon(smallMarkerIcon));

                int area = DataManager.getInstance().inArea();

                if (area != 0) {
                    Log.d("GEO", "adding artifact, ID: " + area);
                    Artifact artifact = DataManager.getInstance().artifactsList.get(area - 1);

                    if (!artifact.getCollected()) {
                        LatLng latLng = new LatLng(artifact.getAlt(), artifact.getLng());
                        map.addMarker(new MarkerOptions()
                                .position(latLng));
                    }
                }

                map.addCircle(new CircleOptions()
                        .center(pos)
                        .radius(75)
                        .strokeColor(Color.RED)
                        .fillColor(0x7FDD0000));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(pos)
                        .zoom(18)
                        .tilt(67.5f)
                        .bearing(314)
                        .build();


                map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                //post a 1 second delay before updating again

            }
            progressHandler.postDelayed(this, 1000);

        }

    };




}
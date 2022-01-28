package com.example.scavengerhunt.Misc;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.scavengerhunt.Activities.HuntActivity;
import com.example.scavengerhunt.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import android.os.Looper;

import java.util.concurrent.TimeUnit;

public class TrackingService extends Service {

    LocationManager locationManager;
    private static final String CHANNEL_ID = "trackerChannel";
    Location lastLocation;

    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;

    private long timeStart;


    public TrackingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(100);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    lastLocation = location;
                }
            }
        };

        startLocationUpdates();


        timeStart = System.currentTimeMillis();

        //return if no permission for location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        //get the repo
        //mRepo = new Repository( this.getApplication() );

        //create a new notification channel (only for Oreo+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            //create notification manager
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);

        }

        //notification intent, when clicked, go back to the tracking activity (singleton)
        Intent notificationIntent = new Intent(this, HuntActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(TrackingService.this,
                0, notificationIntent, 0);

        //build the foreground notification
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Scavenger Hunt")
                .setContentText("Using location services to track movement.")
                .setSmallIcon(R.drawable.camping_icon_13511)
                .setContentIntent(pendingIntent)
                .build();


        //start foreground
        startForeground(1, notification); //start foreground so it doesn't get killed*/
    }

    public class MyBinder extends Binder {
        public TrackingService getService() {
            return TrackingService.this;
        }
    }

    public void setStatus(String status) {

    }

    public Location getLastLocation() {
        return lastLocation;
    }


    private void startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }


    public String getTime() {
        Long time = System.currentTimeMillis() - timeStart;
        String text = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(time),
                TimeUnit.MILLISECONDS.toMinutes(time));
        return text;
    }
}
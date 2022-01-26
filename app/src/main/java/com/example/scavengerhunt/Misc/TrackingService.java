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

import java.util.concurrent.TimeUnit;

public class TrackingService extends Service {

    LocationManager locationManager;
    private static final String CHANNEL_ID = "trackerChannel";
    Location lastLocation;

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
        timeStart = System.currentTimeMillis();
        //create new location manager
        locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationTracker locationListener = new LocationTracker();
        //request location updates at every 10m&5ms
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    5, // minimum time interval between updates
                    10, // minimum distance between updates, in metres
                    locationListener);
        } catch (SecurityException e) {
            Log.d("g53mdp", e.toString());
        }

        //return if no permission for location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


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
                .setContentTitle("Running Tracker")
                .setContentText("Currently tracking movement ")
                .setSmallIcon(R.drawable.ic_launcher_background)
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

    //location tracked implementation
    public class LocationTracker implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            lastLocation = location;
            //on location changed, insert the new location if status = tracking and update last known location
            /*if(status == Status.TRACKING) {
                Log.d("g53mdp", location.getLatitude() + " " + location.getLongitude());
                //mRepo.insert(new GPS((int)mRepo.getLatestId(), location.getTime(), location.getLatitude(), location.getLongitude()));
                //lastLocation = location;
                //addLocation(location);
            }*/
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // information about the signal, i.e. number of satellites
            Log.d("g53mdp", "onStatusChanged: " + provider + " " + status);
        }
        @Override
        public void onProviderEnabled(String provider) {
            // the user enabled (for example) the GPS
            //GPSActive = true;
            Log.d("g53mdp", "onProviderEnabled: " + provider);
        }
        @Override
        public void onProviderDisabled(String provider) {
            // the user disabled (for example) the GPS
            //GPSActive = false;
            Log.d("g53mdp", "onProviderDisabled: " + provider);
            //logLocation();
        }

    }

    public String getTime() {
        Long time = System.currentTimeMillis() - timeStart;
        String text = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(time),
                TimeUnit.MILLISECONDS.toMinutes(time));
        return text;
    }
}
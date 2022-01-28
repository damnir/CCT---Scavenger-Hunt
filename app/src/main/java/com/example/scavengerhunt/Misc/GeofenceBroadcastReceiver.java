package com.example.scavengerhunt.Misc;

import com.example.scavengerhunt.Entities.Action;
import com.example.scavengerhunt.Entities.Scavenger;
import com.example.scavengerhunt.Entities.Session;
import com.example.scavengerhunt.Entities.Site;
import com.example.scavengerhunt.Firebase.Database;
import com.example.scavengerhunt.R;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "GBRC";
    private Context mContext;
    private NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;

        createNotificationChannel();

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceStatusCodes
                    .getStatusCodeString(geofencingEvent.getErrorCode());
            Log.e("TAG", errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER && Scavenger.getInstance().getRole().equals("Discoverer")){
                Log.d("GEO", "entered geofence : " + geofencingEvent.getTriggeringGeofences());
                DataManager.getInstance().setActiveGeofence(Integer.parseInt(geofencingEvent.getTriggeringGeofences().get(0).getRequestId()));
                //createNotification();
                int index = DataManager.getInstance().activeGeofence-1;

                if(!DataManager.getInstance().siteList.get(index).getVisited()) {
                    //notificationManager.notify(0, createNotification());
                    Action action = new Action();
                    action.setType("site");
                    Database.getInstance().newAction(action);
                    DataManager.getInstance().setSiteVoid(true);
                    Session.getInstance().addSite(DataManager.getInstance().siteList.get(index));
                    createSiteLog(DataManager.getInstance().siteList.get(index));
                }
            }
            if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                Log.d("GEO", "exited geofence");
                DataManager.getInstance().setActiveGeofence(0);
            }


        } else {
            Log.e("TAG", "error");
        }
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
            notificationManager = mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public Notification createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.camping_icon_13511)
                .setContentTitle("Artifact Nearby!")
                .setContentText("You discovered a new site, there is an artifact nearby, use the radar to find it!")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        return builder.build();
    }

    public void createSiteLog(Site site) {
        com.example.scavengerhunt.Entities.Log log = new com.example.scavengerhunt.Entities.Log();
        log.setLabel("Description");
        log.setTitle(site.getName());
        log.setDescription(site.getDescription());
        log.setImage(site.getImageUri());
        log.setStamp("Site visited");

        Session.getInstance().addLog(log);

        Database.getInstance().addLog();

    }

}

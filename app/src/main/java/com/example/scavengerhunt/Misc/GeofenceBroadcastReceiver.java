package com.example.scavengerhunt.Misc;

import static android.provider.Settings.System.getString;
import com.example.scavengerhunt.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;


import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "GBRC";
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;

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

            if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER){
                Log.d("GEO", "entered geofence : " + geofencingEvent.getTriggeringGeofences());
                ManualData.getInstance().setActiveGeofence(Integer.parseInt(geofencingEvent.getTriggeringGeofences().get(0).getRequestId()));
                createNotification();
            }
            if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                Log.d("GEO", "exited geofence");
                ManualData.getInstance().setActiveGeofence(0);
            }


        } else {
            Log.e("TAG", "error");
        }
    }

    public void createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.camping_icon_13511)
                .setContentTitle("Artifact Nearby!")
                .setContentText("There is an artifact nearby, use the radar to find it!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }
}

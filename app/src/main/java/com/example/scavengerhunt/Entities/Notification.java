package com.example.scavengerhunt.Entities;

import android.content.Context;

import androidx.core.app.NotificationCompat;

import com.example.scavengerhunt.R;

public class Notification {

    private static Notification INSTANCE = new Notification();


    public Notification() {

    }

    public static Notification getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Notification();
        }
        return INSTANCE;
    }

    public android.app.Notification newSiteNotification(Context mContext, String CHANNEL_ID) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.camping_icon_13511)
                .setContentTitle("Artifact Nearby! - New Site Discovered")
                .setContentText("You discovered a new site, there is an artifact nearby, use the radar to find it!")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        return builder.build();
    }

    public android.app.Notification newArtifactNotification(Context mContext, String CHANNEL_ID, String name) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.camping_icon_13511)
                .setContentTitle("New Artifact Collected: " + name)
                .setContentText("Your team has collected a new artifact. Check log for more details.")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        return builder.build();
    }

    public android.app.Notification newMessageNotification(Context mContext, String CHANNEL_ID, String name, String description) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.camping_icon_13511)
                .setContentTitle("New message from: " + name)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        return builder.build();
    }

    public android.app.Notification newStoryNotification(Context mContext, String CHANNEL_ID) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.camping_icon_13511)
                .setContentTitle("New Story Fragment Added!")
                .setContentText("Check log to see new story fragment.")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        return builder.build();
    }



}

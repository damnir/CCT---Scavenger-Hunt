package com.example.scavengerhunt.ViewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.scavengerhunt.Entities.Session;
import com.example.scavengerhunt.Entities.User;
import com.example.scavengerhunt.Firebase.FirebaseQueryLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StoryViewModel extends ViewModel {
    private Session session = Session.getInstance();

    private DatabaseReference STORY_REF =
            FirebaseDatabase.getInstance().getReference("/active_sessions/" + User.getInstance().getActiveSessionId() + "/story/");

    private FirebaseQueryLiveData storyLiveData = new FirebaseQueryLiveData(STORY_REF);

    @NonNull
    public LiveData<DataSnapshot> getStorySS() {
        Log.d("SC", "VIEMODEL DEBUG: " + STORY_REF);
        return storyLiveData;
    }

    public void formatSession(DataSnapshot dataSnapshot) {
        //Log.d("ONCHANGED", "" + dataSnapshot.getValue(Session.class));
        //session = dataSnapshot.getValue(Session.class);
        //for (Scavenger s : session.getScavengers()) {
        //    Log.d("ONCHANGED", s.getRole() + " " + s.getUser().getName());
        //}

    }

    public void updateDBReference() {
        STORY_REF = FirebaseDatabase.getInstance().getReference("/active_sessions/" + User.getInstance().getActiveSessionId() + "/");
    }
}

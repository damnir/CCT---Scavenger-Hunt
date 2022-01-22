package com.example.scavengerhunt.ViewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.scavengerhunt.Entities.Scavenger;
import com.example.scavengerhunt.Entities.Session;
import com.example.scavengerhunt.Entities.User;
import com.example.scavengerhunt.Firebase.FirebaseQueryLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogViewModel extends ViewModel {

    private Session session = Session.getInstance();

    private DatabaseReference LOG_REF =
            FirebaseDatabase.getInstance().getReference("/active_sessions/" + User.getInstance().getActiveSessionId() + "/log/");

    private FirebaseQueryLiveData logLiveData = new FirebaseQueryLiveData(LOG_REF);

    @NonNull
    public LiveData<DataSnapshot> getLogsSS() {
        Log.d("SC", "VIEMODEL DEBUG: " + LOG_REF);
        return logLiveData;
    }

    public void formatSession(DataSnapshot dataSnapshot) {
        //Log.d("ONCHANGED", "" + dataSnapshot.getValue(Session.class));
        //session = dataSnapshot.getValue(Session.class);
        //for (Scavenger s : session.getScavengers()) {
        //    Log.d("ONCHANGED", s.getRole() + " " + s.getUser().getName());
        //}

    }

    public void updateDBReference() {
        LOG_REF = FirebaseDatabase.getInstance().getReference("/active_sessions/" + User.getInstance().getActiveSessionId() + "/");
    }


}

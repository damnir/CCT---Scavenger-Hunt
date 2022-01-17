package com.example.scavengerhunt.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.scavengerhunt.Entities.Scavenger;
import com.example.scavengerhunt.Entities.Session;
import com.example.scavengerhunt.Entities.User;
import com.example.scavengerhunt.Firebase.FirebaseQueryLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SessionViewModel extends ViewModel {

    //private static String sessionId = User.getInstance().getActiveSessionId();
    private Session session = Session.getInstance();

    private static final DatabaseReference SESSION_USERS_REF =
            FirebaseDatabase.getInstance().getReference("/active_sessions/" + User.getInstance().getActiveSessionId() + "/scavengers/");

    private final FirebaseQueryLiveData usersLiveData = new FirebaseQueryLiveData(SESSION_USERS_REF);

    @NonNull
    public LiveData<DataSnapshot> getUsersLiveDataSS() {
        return usersLiveData;
    }

    public void formatSession(DataSnapshot dataSnapshot) {
        Log.d("ONCHANGED", "" + dataSnapshot.getValue(Session.class));
        /*session = dataSnapshot.getValue(Session.class);
        for (Scavenger s : session.getScavengers() ) {
            Log.d("ONCHANGED", s.getRole() + " " + s.getUserName());
    */}


    public void joinSession(String sessionId) {


    }
}



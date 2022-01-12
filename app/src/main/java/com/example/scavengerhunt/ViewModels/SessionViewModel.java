package com.example.scavengerhunt.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.scavengerhunt.Entities.Scavenger;
import com.example.scavengerhunt.Entities.User;
import com.example.scavengerhunt.Firebase.FirebaseQueryLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SessionViewModel extends ViewModel {

    //private static String sessionId = User.getInstance().getActiveSessionId();

    private static final DatabaseReference SESSION_USERS_REF =
            FirebaseDatabase.getInstance().getReference("/active_sessions/" + User.getInstance().getActiveSessionId() + "/scavengers/");

    private final FirebaseQueryLiveData usersLiveData = new FirebaseQueryLiveData(SESSION_USERS_REF);

    @NonNull
    public LiveData<DataSnapshot> getUsersLiveDataSS() {
        return usersLiveData;
    }


}


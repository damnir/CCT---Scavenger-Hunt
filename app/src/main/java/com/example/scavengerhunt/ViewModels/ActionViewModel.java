package com.example.scavengerhunt.ViewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.scavengerhunt.Entities.User;
import com.example.scavengerhunt.Firebase.FirebaseQueryLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ActionViewModel extends ViewModel {
    private DatabaseReference ACT_REF =
            FirebaseDatabase.getInstance().getReference("/active_sessions/" + User.getInstance().getActiveSessionId() + "/actions/");

    private FirebaseQueryLiveData actLiveData = new FirebaseQueryLiveData(ACT_REF);

    @NonNull
    public LiveData<DataSnapshot> getActionsLiveData() {
        Log.d("SC", "VIEMODEL DEBUG: " + ACT_REF);
        return actLiveData;
    }

    public void updateDBReference() {
        ACT_REF = FirebaseDatabase.getInstance().getReference("/active_sessions/" + User.getInstance().getActiveSessionId() + "/actions/");
    }

}

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

public class MessagesViewModel extends ViewModel {
    private DatabaseReference MSG_REF =
            FirebaseDatabase.getInstance().getReference("/active_sessions/" + User.getInstance().getActiveSessionId() + "/messages/");

    private FirebaseQueryLiveData msgLiveData = new FirebaseQueryLiveData(MSG_REF);

    @NonNull
    public LiveData<DataSnapshot> getMsgs() {
        Log.d("SC", "VIEMODEL DEBUG: " + MSG_REF);
        return msgLiveData;
    }

    public void updateDBReference() {
        MSG_REF = FirebaseDatabase.getInstance().getReference("/active_sessions/" + User.getInstance().getActiveSessionId() + "/messages/");
    }
}

package com.example.scavengerhunt.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.scavengerhunt.Entities.Scavenger;
import com.example.scavengerhunt.Entities.User;

public class SessionViewModel extends AndroidViewModel {

    LiveData<Scavenger> scavengers;

    public SessionViewModel(@NonNull Application application) {
        super(application);
    }


}

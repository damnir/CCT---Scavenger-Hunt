package com.example.scavengerhunt.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.scavengerhunt.Entities.Scavenger;
import com.example.scavengerhunt.Entities.Session;
import com.example.scavengerhunt.Entities.User;
import com.example.scavengerhunt.Firebase.Database;
import com.example.scavengerhunt.R;
import com.example.scavengerhunt.ui.main.SessionViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.annotations.Nullable;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Locale;
import java.util.Random;
import java.util.stream.IntStream;

public class NewSessionActivity extends AppCompatActivity {

    private Session session;
    private Database dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_session);

        dbRef = Database.getInstance();

        String newSessionId = generateSessionId();
        User.getInstance().setActiveSessionId(newSessionId);
        session = new Session(newSessionId);
        session.setOwner(User.getInstance());
        session.addScavenger(new Scavenger(User.getInstance()));

        dbRef.newSession(session);

        SessionViewModel viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SessionViewModel.class);


        LiveData<DataSnapshot> liveData = viewModel.getDataSnapshotLiveData();

        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    // update the UI here with values in the snapshot
                    //String ticker = dataSnapshot.child("ticker").getValue(String.class);
                    //tvTicker.setText(ticker);
                    //Float price = dataSnapshot.child("price").getValue(Float.class);
                    //tvPrice.setText(String.format(Locale.getDefault(), "%.2f", price));
                    Log.d("ONCHANGED: ", String.valueOf(dataSnapshot));
                }
            }
        });
    }


    public String generateSessionId() {
        Random rand = new Random();
        String id = "";
        for(int i = 0; i < 6; i++) id += rand.nextInt(9);
        return id;
    }


}
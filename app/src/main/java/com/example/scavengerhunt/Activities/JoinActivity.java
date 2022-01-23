package com.example.scavengerhunt.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.scavengerhunt.Entities.Scavenger;
import com.example.scavengerhunt.Entities.Session;
import com.example.scavengerhunt.Entities.User;
import com.example.scavengerhunt.Firebase.Database;
import com.example.scavengerhunt.R;
import com.example.scavengerhunt.ViewModels.SessionViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.annotations.Nullable;

public class JoinActivity extends AppCompatActivity {

    private Database dbRef;
    private EditText sessionInput;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        session = Session.getInstance();

        //Scavenger scavenger = new Scavenger(User.getInstance());
        dbRef = Database.getInstance();
        sessionInput = findViewById(R.id.inputSessionId);

        //dbRef.joinSession("123815", scavenger);

        SessionViewModel viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SessionViewModel.class);

        LiveData<DataSnapshot> liveData = viewModel.getUsersLiveDataSS();

        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    // update the UI here with values in the snapshot
                    //String ticker = dataSnapshot.child("ticker").getValue(String.class);
                    //tvTicker.setText(ticker);
                    //Float price = dataSnapshot.child("price").getValue(Float.class);
                    //tvPrice.setText(String.format(Locale.getDefault(), "%.2f", price));
                    session = dataSnapshot.getValue(Session.class);

                    Log.d("ONCHANGED: ", String.valueOf(dataSnapshot));

                }
            }
        });

        //dbRef.
    }


    public void joinSessionClick(View v) {
        String id = sessionInput.getText().toString();
        User.getInstance().setActiveSessionId(id);
        Scavenger scavenger = Scavenger.getInstance();
        scavenger.setUser(User.getInstance());

        User.getInstance().setActiveSessionId(id);
        dbRef.joinSession(id, scavenger);

        changeActivity();

    }

    public void changeActivity() {
        Session session = Session.getInstance();
        session.replaceInstance(dbRef.replaceInstace());
        Log.d("SC", "Session ID: " + session.getSessionId());
        for (Scavenger s: session.getScavengers() ){
            Log.d("SC", "Scavengers: " + s.getUser().getName() + " role: " + s.getRole());
        }

        Log.d("SC", "USER JOIN: " + User.getInstance().getActiveSessionId());

        Intent intent = new Intent(this, NewSessionActivity.class);
        startActivity(intent);
    }





}
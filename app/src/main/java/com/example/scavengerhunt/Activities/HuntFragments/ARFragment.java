package com.example.scavengerhunt.Activities.HuntFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.scavengerhunt.Activities.AugmentedRealityActivity;
import com.example.scavengerhunt.Entities.Scavenger;
import com.example.scavengerhunt.Firebase.Database;
import com.example.scavengerhunt.Misc.DataManager;
import com.example.scavengerhunt.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ARFragment extends Fragment {

    private View v;

    private TextView text;
    private FloatingActionButton fob;

    private final String disabledText = "There are no artifacts nearby";
    private final String enabledText = "Open AR Scanner";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_a_r, container, false);
        text = view.findViewById(R.id.AR_text);
        fob = view.findViewById(R.id.AR_button);

        progressHandler.post(inActiveGeo);
        return view;
    }

    private Handler progressHandler = new Handler();
    //runnable, updated every second to give live UI updates
    private Runnable inActiveGeo = new Runnable() {
        @Override
        public void run() {
            DataManager dm = DataManager.getInstance();
            if(dm.activeGeofence != 0)
            {
                text.setText(enabledText);
                fob.setEnabled(true);
            }
            else {
                fob.setEnabled(false);
                text.setText(disabledText);
            }
            progressHandler.postDelayed(this, 1500);

        }
    };



}
package com.example.scavengerhunt.Activities.HuntFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scavengerhunt.Activities.AugmentedRealityActivity;
import com.example.scavengerhunt.R;

public class ARFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_a_r, container, false);
    }

    public void onButtonClick(View v) {
        Intent intent = new Intent(getActivity(), AugmentedRealityActivity.class);
        startActivity(intent);
    }
}
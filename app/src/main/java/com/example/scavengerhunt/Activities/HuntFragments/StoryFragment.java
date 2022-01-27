package com.example.scavengerhunt.Activities.HuntFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scavengerhunt.Entities.Log;
import com.example.scavengerhunt.Entities.Session;
import com.example.scavengerhunt.Entities.Story;
import com.example.scavengerhunt.Misc.Adapters.LogAdapter;
import com.example.scavengerhunt.Misc.Adapters.StoryAdapter;
import com.example.scavengerhunt.R;
import com.example.scavengerhunt.ViewModels.LogViewModel;
import com.example.scavengerhunt.ViewModels.StoryViewModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.List;


public class StoryFragment extends Fragment implements OnMapReadyCallback {

    private StoryAdapter adapter;
    private Session session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story, container, false);

        session = Session.getInstance();

        adapter = new StoryAdapter(getActivity());

        RecyclerView recyclerView = view.findViewById(R.id.story_recycler);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        StoryViewModel viewModel = new ViewModelProvider(getActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(StoryViewModel.class);

        viewModel.updateDBReference();

        LiveData<DataSnapshot> liveData = viewModel.getStorySS();

        liveData.observe(this, dataSnapshot -> {
            if (dataSnapshot != null) {
                if(!dataSnapshot.hasChildren()){
                    return;
                }
                //session =  dataSnapshot.getValue(Session.class);
                GenericTypeIndicator<List<Story>> t = new GenericTypeIndicator<List<Story>>() {};
                session.setStories(dataSnapshot.getValue(t));
                adapter.setData(session.getStories());
            }
        });

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }
}
package com.example.scavengerhunt.Activities.HuntFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scavengerhunt.Entities.Log;
import com.example.scavengerhunt.Entities.Session;
import com.example.scavengerhunt.LogAdapter;
import com.example.scavengerhunt.R;
import com.example.scavengerhunt.Entities.Log;
import com.example.scavengerhunt.SessionAdapter;
import com.example.scavengerhunt.ViewModels.LogViewModel;
import com.example.scavengerhunt.ViewModels.SessionViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;


public class LogFragment extends Fragment {



    private LogAdapter adapter;
    private Session session = Session.getInstance();

    public LogFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log, container, false);

        adapter = new LogAdapter(getActivity());

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView2);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        LogViewModel viewModel = new ViewModelProvider(getActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(LogViewModel.class);

        viewModel.updateDBReference();

        LiveData<DataSnapshot> liveData = viewModel.getLogsSS();

        liveData.observe(this, dataSnapshot -> {
            if (dataSnapshot != null) {
                //session =  dataSnapshot.getValue(Session.class);
                GenericTypeIndicator<List<Log>> t = new GenericTypeIndicator<List<Log>>() {};
                session.logs = dataSnapshot.getValue(t);
                adapter.setData(session.getLogs());
            }
        });

        return view;
    }
}
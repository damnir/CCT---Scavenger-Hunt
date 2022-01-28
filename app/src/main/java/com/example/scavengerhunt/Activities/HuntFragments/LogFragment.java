package com.example.scavengerhunt.Activities.HuntFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.scavengerhunt.Entities.Artifact;
import com.example.scavengerhunt.Entities.Log;
import com.example.scavengerhunt.Entities.Session;
import com.example.scavengerhunt.Misc.Adapters.LogAdapter;
import com.example.scavengerhunt.R;
import com.example.scavengerhunt.ViewModels.LogViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;
import com.squareup.picasso.Picasso;


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
                if(!dataSnapshot.hasChildren()){
                    return;
                }
                //session =  dataSnapshot.getValue(Session.class);
                GenericTypeIndicator<List<Log>> t = new GenericTypeIndicator<List<Log>>() {};
                session.logs = dataSnapshot.getValue(t);
                adapter.setData(session.getLogs());
            }
        });

        adapter.setClickListener(log -> {
            inflatePopup(log);
        });

        return view;
    }

    public void inflatePopup(Log log) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_view, null);

        TextView stamp = popupView.findViewById(R.id.popup_stamp);
        TextView description = popupView.findViewById(R.id.popup_description);
        TextView title = popupView.findViewById(R.id.popup_title);
        ImageView imageView = popupView.findViewById(R.id.popup_image);

        stamp.setText("Site Discovered!");
        description.setText(log.getDescription());
        title.setText(log.getTitle());
        try{
            Picasso.get().load(log.getImage()).into(imageView);
        }catch (NullPointerException e){
            android.util.Log.d("IMAGE", "Exception: " + e);
        };

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(getActivity().findViewById(android.R.id.content).getRootView()
                , Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        Button button = popupView.findViewById(R.id.popup_button);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
}
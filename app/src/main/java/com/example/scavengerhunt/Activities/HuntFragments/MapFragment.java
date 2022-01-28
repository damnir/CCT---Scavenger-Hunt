package com.example.scavengerhunt.Activities.HuntFragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.scavengerhunt.Entities.Action;
import com.example.scavengerhunt.Entities.Message;
import com.example.scavengerhunt.Entities.Scavenger;
import com.example.scavengerhunt.Entities.Session;
import com.example.scavengerhunt.Entities.User;
import com.example.scavengerhunt.Firebase.Database;
import com.example.scavengerhunt.Misc.Adapters.MessageAdapter;
import com.example.scavengerhunt.Misc.Adapters.SessionAdapter;
import com.example.scavengerhunt.Misc.LatLngConverter;
import com.example.scavengerhunt.R;
import com.example.scavengerhunt.Misc.TrackingService;
import com.example.scavengerhunt.ViewModels.LogViewModel;
import com.example.scavengerhunt.ViewModels.MessagesViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MapFragment extends Fragment implements OnMapReadyCallback{

    public MapFragment() {
        // Required empty public constructor
    }

    private Location location;
    private TrackingService trackingService;
    private Activity activity;
    private GoogleMap map;
    private BitmapDescriptor smallMarkerIcon;
    private TextView coords;
    private LatLngConverter latLngConverter;
    private TextView distance;
    private TextView finalDestText;
    private TextView time;
    private FloatingActionButton msgButton;
    private MessageAdapter messageAdapter;

    private SessionAdapter adapter;
    private List<Message> messages;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();

        Intent intent = new Intent(getActivity(), TrackingService.class);
        activity.startService(intent);
        activity.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("check","onCreateView");
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_map, container, false);
        // Gets the MapView from the XML layout and creates it
        final SupportMapFragment myMAPF = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        myMAPF.getMapAsync(this);
        msgButton = view.findViewById(R.id.map_fob);
        msgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflatePopup();
            }
        });

        messages = new ArrayList<>();

        latLngConverter = new LatLngConverter();
        //TEMPORARY VARIABLE - Reuse destination site object to get coordinates pls
        adapter = new SessionAdapter(getActivity());
        messageAdapter = new MessageAdapter(getActivity());

        RecyclerView recyclerView = view.findViewById(R.id.map_recycler);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        MessagesViewModel viewModel = new ViewModelProvider(getActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(MessagesViewModel.class);

        viewModel.updateDBReference();

        LiveData<DataSnapshot> liveData = viewModel.getMsgs();

        liveData.observe(this, dataSnapshot -> {
            if (dataSnapshot != null) {
                if(!dataSnapshot.hasChildren()){
                    return;
                }
                Session session = Session.getInstance();

                session.messages.clear();

                //session =  dataSnapshot.getValue(Session.class);
                for(DataSnapshot c : dataSnapshot.getChildren()) {
                    session.messages.add(c.getValue(Message.class));
                }

                messageAdapter.setData(session.messages);
            }
        });

        setData();

        return  view;
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        //on service connected bind to the service and getService
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TrackingService.MyBinder myBinder = (TrackingService.MyBinder) service;
            trackingService = myBinder.getService();
            trackingService.setStatus("TRACKING"); //start tracking
            progressHandler.postDelayed(updateProgress, 2000); //update UI based on progress

            //progressHandler.postDelayed(updateProgress, 0); //update UI based on progress
            //update(); //call update to update all UI elements
        }
        //on disconnected null the service reference
        @Override
        public void onServiceDisconnected(ComponentName name) {
            trackingService = null;
        }
    };


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        Log.d("MAP", "MAP READY");

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable. user);
        Bitmap smallMarker = Bitmap.createScaledBitmap(icon, 75, 75, false);
        smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);

    }

    private Handler progressHandler = new Handler();
    //runnable, updated every second to give live UI updates
    private Runnable updateProgress = new Runnable() {
        @Override
        public void run() {
            location = trackingService.getLastLocation();
            if(location == null) return;
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            Scavenger.getInstance().setaLat(lat);
            Scavenger.getInstance().setLng(lng);

            Database.getInstance().updateLocation(lat, lng);

            //LatLng pos = new LatLng(lat, lng);
            map.clear();
            LatLng pos = new LatLng(lat, lng);
            for(Scavenger c : Database.getInstance().getAllScavengers()) {
                pos = new LatLng(c.getaLat(), c.getLng());
                map.addMarker(new MarkerOptions()
                        .position(pos)
                        .title(c.getUser().getName())
                        .icon(smallMarkerIcon));
            }


            Log.d("MAP", "LatLng" + lat + lng);



            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(pos, 16);
            map.moveCamera(cameraUpdate);

            String covertedCoords = latLngConverter.latnglToDMS((float)location.getLatitude(),
                    (float)location.getLongitude());

            coords = getActivity().findViewById(R.id.navigate_coords);
            distance = getActivity().findViewById(R.id.navigate_distance);
            finalDestText = getActivity().findViewById(R.id.navigate_final);
            time = getActivity().findViewById(R.id.navigate_time);

            try {
                coords.setText(covertedCoords);
                distance.setText(distance(location, 52.935587, -1.194325));
                time.setText(trackingService.getTime());
                finalDestText.setText(latLngConverter.latnglToDMS((float)52.935587, (float)-1.194325));

            }catch (NullPointerException ignored){}

            //post a 1 second delay before updating again
            progressHandler.postDelayed(this, 3500);

        }
    };

    private String distance(Location location, Double destinationLat, Double destinationLng) {
        Location destination = new Location("dest");
        destination.setLatitude(destinationLat);
        destination.setLongitude(destinationLng);
        float distance = location.distanceTo(destination);
        if(distance < 1000) {
            return ((int)distance + "m away");
        }
        else{
            return (distance/1000 + "km away");
        }
    }

    private void setData() {
        List<Scavenger> data = new ArrayList<>();

        Database.getInstance().getDBRef().child("active_sessions")
                .child(User.getInstance().getActiveSessionId()).child("scavengers").get().
                addOnCompleteListener(task -> {
                    DataSnapshot ds = task.getResult();
                    for(DataSnapshot c : ds.getChildren()) {
                        data.add(c.getValue(Scavenger.class));
                    }
                    adapter.setData(data);
                });
    }


    @SuppressLint("ClickableViewAccessibility")
    public void inflatePopup() {
        Log.d("MESSAGE", "inflateCalled");

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.chat_view, null);

        TextView stamp = popupView.findViewById(R.id.message_stamp);
        TextView messageText = popupView.findViewById(R.id.messages_input);

        Message message = new Message();


        RecyclerView recyclerView = popupView.findViewById(R.id.messages_recycler);

        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
        ImageButton button = popupView.findViewById(R.id.messages_back);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
        ImageButton buttonSend = popupView.findViewById(R.id.message_send);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message.setMessage(messageText.getText().toString());
                message.setStamp(User.getInstance().getName());
                Session.getInstance().addMessage(message);
                Log.d("MESSAGE", "messgae sent");

                Action action = new Action();
                action.setType("message");
                action.setData1(message.getStamp());
                action.setData2(message.getMessage());
                Database.getInstance().newAction(action);
                Database.getInstance().addMessage(message);
            }
        });
    }


}
package com.example.scavengerhunt.Misc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scavengerhunt.Entities.Scavenger;
import com.example.scavengerhunt.Entities.Story;
import com.example.scavengerhunt.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.DataViewHolder>{


    private List<Story> data;
    private Context context;
    private LayoutInflater layoutInflater;

    public StoryAdapter(Context context) {
        this.data = new ArrayList<>();
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public StoryAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the itemView
        View itemView = layoutInflater.inflate(R.layout.story_view, parent, false);
        return new StoryAdapter.DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryAdapter.DataViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    //set the data once observer has notified in the activity
    public void setData(List<Story> newData) {
        if (data != null) {
            data.clear();
            data.addAll(newData);
            notifyDataSetChanged();
        } else {
            data = newData;
        }
    }

    //recycler viewholder
    class DataViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback{

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);

        }

        TextView stamp = itemView.findViewById(R.id.story_stamp);
        TextView title = itemView.findViewById(R.id.story_title);
        TextView description = itemView.findViewById(R.id.story_description);
        ImageView image = itemView.findViewById(R.id.story_image);
        MapView map = itemView.findViewById(R.id.mapView);

        Story mStory;

        public void bind(Story story) {
            /*if(scavenger != null) {
                role.setText(scavenger.getRole());
                name.setText(scavenger.getUser().getName());
            }*/
            mStory = story;
            if(story != null) {
                stamp.setText(story.getStamp());
                title.setText(story.getName());
                description.setText(story.getDescription());
                map.onCreate(null);
                //map.getMapAsync(setOnMapReadyCallback(story.getLat(), story.getLng(), story.getName()));
                map.getMapAsync(this);
                try{
                    android.util.Log.d("IMAGE", "URI: " + story.getUrl());
                    Picasso.get().load(story.getUrl()).into(image);
                    //Picasso.get().load("https://i.imgur.com/DvpvklR.png").into(image);
                    //Picasso.get().load(R.drawable.instagram_icon_969).into(image);
                }catch (NullPointerException e){
                    android.util.Log.d("IMAGE", "Exception: " + e);
                };
            }
        }

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            LatLng pos = new LatLng(mStory.getLat(), mStory.getLng());

            googleMap.addMarker(new MarkerOptions()
                    .position(pos)
                    .title(mStory.getName())
            );

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(pos, 14);
            googleMap.moveCamera(cameraUpdate);
        }
    }



    public OnMapReadyCallback setOnMapReadyCallback(double lat, double lng, String site) {
        return new OnMapReadyCallback() {
            LatLng pos = new LatLng(lat, lng);
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                googleMap.addMarker(new MarkerOptions()
                        .position(pos)
                        .title(site));
            }
        };
    }
}
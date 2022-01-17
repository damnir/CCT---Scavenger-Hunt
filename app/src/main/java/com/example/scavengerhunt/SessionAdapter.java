package com.example.scavengerhunt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scavengerhunt.Entities.Scavenger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.DataViewHolder>{


    //private List<Run> data;
    private List<Scavenger> data;
    private Context context;
    private LayoutInflater layoutInflater;
    //item click listeners for specific entires/favourite button
    private SessionAdapter.ItemClickListener clickListener;
    private SessionAdapter.ItemClickListener favouriteListener;


    public SessionAdapter(Context context) {
        this.data = new ArrayList<>();
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public SessionAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the itemView
        View itemView = layoutInflater.inflate(R.layout.scavengers_view, parent, false);
        return new SessionAdapter.DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionAdapter.DataViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    //set the data once observer has notified in the activity
    public void setData(List<Scavenger> newData) {
        if (data != null) {
            data.clear();
            data.addAll(newData);
            notifyDataSetChanged();
        } else {
            data = newData;
        }
    }

    //recycler viewholder
    class DataViewHolder extends RecyclerView.ViewHolder{

        /*TextView dateText;
        TextView distanceText;
        TextView durationText;
        ToggleButton favButton;*/

        TextView name;
        TextView role;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            //references to all fields in the view
            /*dateText = itemView.findViewById(R.id.rv_date);
            distanceText = itemView.findViewById(R.id.rv_distance);
            durationText = itemView.findViewById(R.id.rv_time);
            favButton = itemView.findViewById(R.id.toggleButton);*/
            name = itemView.findViewById(R.id.scavName);
            role = itemView.findViewById(R.id.scavRole);

            //on click listener returns position of the item (run id)
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                clickListener.onItemClick(data.get(position));
            });
        }

        public void bind(Scavenger scavenger) {
            if(scavenger != null) {
                //UPDATE the view UI elements if run != 0
                /*double time = run.getTime();
                String duration = String.format("%02d:%02d:%02d",
                        TimeUnit.SECONDS.toHours((long) time),
                        TimeUnit.SECONDS.toMinutes((long) time) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours((long) time)),
                        TimeUnit.SECONDS.toSeconds((long) time) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes((long) time)));

                durationText.setText(duration);
                distanceText.setText( Math.round((run.getDistance()/1000) * 100.00)/100.00 + "km" );
                dateText.setText(run.getDate());*/

                //name.setText(scavenger.getUserName());
                role.setText(scavenger.getRole());

                //activate the button if the run is marked as favourite
                /*if (run.getFavourite()){
                    favButton.setChecked(true);
                }*/

                //set the on click listener for the favourite button and update the run accordingly
            }
        }
    }

    //entry click listener
    void setClickListener(SessionAdapter.ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
    //favourite button click listener
    void setFavouriteListener(SessionAdapter.ItemClickListener favClickListener) {
        this.favouriteListener = favClickListener;
    }

    //interface to be implemented in the main activity to register item clicks from the adapter list
    public interface ItemClickListener {
        void onItemClick(Scavenger scavenger);
    }

}

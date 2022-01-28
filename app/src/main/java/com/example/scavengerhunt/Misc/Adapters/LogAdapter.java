package com.example.scavengerhunt.Misc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scavengerhunt.Entities.Log;
import com.example.scavengerhunt.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.DataViewHolder>{
    private List<Log> data;
    private Context context;
    private LayoutInflater layoutInflater;
    private LogAdapter.ItemClickListener clickListener;


    public LogAdapter(Context context) {
        this.data = new ArrayList<>();
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public LogAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the itemView
        View itemView = layoutInflater.inflate(R.layout.logs_view, parent, false);
        return new LogAdapter.DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LogAdapter.DataViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    //set the data once observer has notified in the activity
    public void setData(List<Log> newData) {
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

        TextView title = itemView.findViewById(R.id.log_title);
        TextView label = itemView.findViewById(R.id.log_label);
        TextView description = itemView.findViewById(R.id.log_description);
        TextView stamp = itemView.findViewById(R.id.log_stamp);
        ImageView image = (ImageView) itemView.findViewById(R.id.log_image);



        public DataViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                clickListener.onItemClick(data.get(position));
            });
            /*
            name = itemView.findViewById(R.id.scavName);
            role = itemView.findViewById(R.id.scavRole);
            */
        }

        public void bind(Log log) {

            if( log != null) {
                title.setText(log.getTitle());
                label.setText(log.getLabel());
                description.setText(log.getDescription());
                stamp.setText(log.getStamp());

                //Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable._0trophy);
                //Bitmap smallMarker = Bitmap.createScaledBitmap(icon, 135, 135, false);

                //image.setImageBitmap(smallMarker);

                try{
                    android.util.Log.d("IMAGE", "URI: " + log.getImage());
                    Picasso.get().load(log.getImage()).into(image);
                    //Picasso.get().load("https://i.imgur.com/DvpvklR.png").into(image);
                    //Picasso.get().load(R.drawable.instagram_icon_969).into(image);
                }catch (NullPointerException e){
                    android.util.Log.d("IMAGE", "Exception: " + e);
                };

            }

        }
    }

    //entry click listener
    public void setClickListener(LogAdapter.ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


    //interface to be implemented in the main activity to register item clicks from the adapter list
    public interface ItemClickListener {
        void onItemClick(Log log);
    }
}

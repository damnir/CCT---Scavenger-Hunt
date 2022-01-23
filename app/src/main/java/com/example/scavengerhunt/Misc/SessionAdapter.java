package com.example.scavengerhunt.Misc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scavengerhunt.Entities.Scavenger;
import com.example.scavengerhunt.R;

import java.util.ArrayList;
import java.util.List;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.DataViewHolder>{


    private List<Scavenger> data;
    private Context context;
    private LayoutInflater layoutInflater;

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

        TextView name;
        TextView role;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.scavName);
            role = itemView.findViewById(R.id.scavRole);
        }

        public void bind(Scavenger scavenger) {
            if(scavenger != null) {
                role.setText(scavenger.getRole());
                name.setText(scavenger.getUser().getName());
            }
        }
    }


}

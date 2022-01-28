package com.example.scavengerhunt.Misc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scavengerhunt.Entities.Artifact;
import com.example.scavengerhunt.Entities.Log;
import com.example.scavengerhunt.Entities.Site;
import com.example.scavengerhunt.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CompactSiteAdapter extends RecyclerView.Adapter<CompactSiteAdapter.DataViewHolder>{
    private List<Site> data;
    private Context context;
    private LayoutInflater layoutInflater;
    private CompactSiteAdapter.ItemClickListener clickListener;


    public CompactSiteAdapter(Context context) {
        this.data = new ArrayList<>();
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public CompactSiteAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the itemView
        View itemView = layoutInflater.inflate(R.layout.site_compact, parent, false);
        return new CompactSiteAdapter.DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CompactSiteAdapter.DataViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    //set the data once observer has notified in the activity
    public void setData(List<Site> newData) {
        if (data != null) {
            data.clear();
            data.addAll(newData);
            notifyDataSetChanged();
        } else {
            data = newData;
        }
    }

    //recycler viewholder
    class DataViewHolder extends RecyclerView.ViewHolder {

        TextView title;


        public DataViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                clickListener.onItemClick(data.get(position));
            });
            title = itemView.findViewById(R.id.site_text);
        }

        public void bind(Site site) {

            if (site != null) {

                title.setText(site.getName());


            }
        }
    }

    //entry click listener
    public void setClickListener(CompactSiteAdapter.ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


    //interface to be implemented in the main activity to register item clicks from the adapter list
    public interface ItemClickListener {
        void onItemClick(Site log);
    }

}


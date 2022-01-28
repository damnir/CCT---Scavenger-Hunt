package com.example.scavengerhunt.Misc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scavengerhunt.Entities.Message;
import com.example.scavengerhunt.Entities.Story;
import com.example.scavengerhunt.R;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.DataViewHolder>{


    private List<Message> data;
    private Context context;
    private LayoutInflater layoutInflater;

    public MessageAdapter(Context context) {
        this.data = new ArrayList<>();
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public MessageAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the itemView
        View itemView = layoutInflater.inflate(R.layout.message_view, parent, false);
        return new MessageAdapter.DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.DataViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    //set the data once observer has notified in the activity
    public void setData(List<Message> newData) {
        if (data != null) {
            data.clear();
            data.addAll(newData);
            notifyDataSetChanged();
        } else {
            data = newData;
        }
    }
    TextView stamp;
    TextView messageText;

    //recycler viewholder
    class DataViewHolder extends RecyclerView.ViewHolder {

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            stamp = itemView.findViewById(R.id.message_stamp);
            messageText = itemView.findViewById(R.id.message_message);
        }

        Message mMessage;

        public void bind(Message message) {

            mMessage = message;
            if(message != null) {
                stamp.setText(message.getStamp());
                messageText.setText(message.getMessage());
            }
        }


    }




}

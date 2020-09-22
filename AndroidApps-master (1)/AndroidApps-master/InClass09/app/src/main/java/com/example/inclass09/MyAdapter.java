package com.example.inclass09;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public  class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    ArrayList<MessageInfo> messagelist;
    ItemClickListener itemClickListener;

    public MyAdapter(ArrayList<MessageInfo> messagelist, ItemClickListener itemClickListener) {
        this.messagelist = messagelist;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v, this.itemClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MessageInfo message = messagelist.get(position);

        holder.subject.setText(message.subject);
        holder.date.setText(message.createdat);


    }

    @Override
    public int getItemCount() {
        return this.messagelist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView subject, date;
        ImageView deleteimage;

        public MyViewHolder(@NonNull View itemView, final ItemClickListener itemClickListener) {
            super(itemView);

            subject = itemView.findViewById(R.id.subject);
            date = itemView.findViewById(R.id.date);

            deleteimage = itemView.findViewById(R.id.delete);

            deleteimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onDeleteClick(getAdapterPosition());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onDisplayClick(getAdapterPosition());
                }
            });

        }
    }

    public interface ItemClickListener {
        void onDeleteClick(int position);
        void onDisplayClick(int position);
    }


}

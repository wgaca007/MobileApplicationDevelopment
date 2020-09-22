package com.example.inclass13;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TripRecyclerViewAdapter extends RecyclerView.Adapter<TripRecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> tripDetails;
    private LayoutInflater mInflater;


    public TripRecyclerViewAdapter(MainActivity mainActivity, ArrayList<String> recyclerList) {
        this.mInflater=LayoutInflater.from(mainActivity);
        this.tripDetails=recyclerList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String city = tripDetails.get(position);
        holder.placename.setText(city);
    }

    @Override
    public int getItemCount() {
        return tripDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView placename,tripname;
        ImageView pin,addPlace;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placename=itemView.findViewById(R.id.placeName);
            tripname=itemView.findViewById(R.id.tripName);
            pin = itemView.findViewById(R.id.pin);
            addPlace=itemView.findViewById(R.id.addPlaces);
            pin.setOnClickListener(this);
            addPlace.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}

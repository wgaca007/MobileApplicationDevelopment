package com.example.midterm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    ArrayList<Weather> sources;
    TrackClickListener forecastActivity;

    public MyAdapter(ArrayList<Weather> sources, ForecastActivity forecastActivity) {
        this.sources = sources;
        this.forecastActivity = (TrackClickListener) forecastActivity;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v, forecastActivity);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CardView card;
        LinearLayout ll1;
        TextView time,temp,humidity,desc;
        ImageView imageView;
        Weather weather;
        City city;
        public MyViewHolder(@NonNull View itemView, TrackClickListener forecastActivity) {
            super(itemView);
        }
    }

    public interface TrackClickListener {
        void onTrackClick(int position);
    }

}

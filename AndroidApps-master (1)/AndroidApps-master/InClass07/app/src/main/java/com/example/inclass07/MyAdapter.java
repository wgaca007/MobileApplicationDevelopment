package com.example.inclass07;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    ArrayList<Song> tracklists;
    TrackClickListener trackClickListener;

    public MyAdapter(ArrayList<Song> tracklists, TrackClickListener trackClickListener) {
        this.tracklists = tracklists;
        this.trackClickListener = trackClickListener;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v, trackClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        Song item = this.tracklists.get(position);

        holder.trackname.setText("Track: " + item.title);
        holder.artistname.setText("Artist: " + item.artistname);
        holder.albumname.setText("Album: " + item.albumname);
        holder.date.setText("Date: " +item.date);

    }


    @Override
    public int getItemCount() {
        return this.tracklists.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView trackname, artistname, albumname, date;
        public Button button;

        TrackClickListener trackClickListener;

        public MyViewHolder(View v, TrackClickListener trackClickListener) {
            super(v);

            trackname = v.findViewById(R.id.mainlayout).findViewById(R.id.trackartist).findViewById(R.id.trackname);
            artistname = v.findViewById(R.id.mainlayout).findViewById(R.id.trackartist).findViewById(R.id.artistname);

            albumname = v.findViewById(R.id.mainlayout).findViewById(R.id.albumdate).findViewById(R.id.albumname);
            date = v.findViewById(R.id.mainlayout).findViewById(R.id.albumdate).findViewById(R.id.date);

            this.trackClickListener = trackClickListener;

            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            this.trackClickListener.onTrackClick(getAdapterPosition());
        }
    }

    public interface TrackClickListener {
        void onTrackClick(int position);
    }
}

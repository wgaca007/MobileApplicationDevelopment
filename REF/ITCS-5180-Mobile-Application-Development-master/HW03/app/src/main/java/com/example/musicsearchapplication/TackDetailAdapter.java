package com.example.musicsearchapplication;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.support.annotation.NonNull;
import android.widget.TextView;

import java.util.List;

public class TackDetailAdapter extends ArrayAdapter<TrackDetails> {

    public TackDetailAdapter(@NonNull Context context, int resource, @NonNull List<TrackDetails> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TrackDetails trackDetails = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.content_track_detail_display,parent,false);
        }

        TextView textView_track = convertView.findViewById(R.id.textView_TrackList);
        TextView textView_price = convertView.findViewById(R.id.textView_PriceList);
        TextView textView_artist = convertView.findViewById(R.id.textView_ArtistList);
        TextView textView_date = convertView.findViewById(R.id.textView_DateList);

        textView_track.setText(trackDetails.getTrackName());
        textView_price.setText(trackDetails.getTrack_Price());
        textView_artist.setText(trackDetails.getArtist());
        textView_date.setText(trackDetails.getReleaseDate());

        return convertView;
    }
}

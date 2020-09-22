package com.example.midtermtemplate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class TrackAdapter extends ArrayAdapter<Track> {

    public TrackAdapter(@NonNull Context context, int resource, @NonNull List<Track> objects) {
        super(context, resource, objects);
    }

    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {



        Track track = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.song_row, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.song = (TextView) convertView.findViewById(R.id.tvTrackName);
            viewHolder.artist = (TextView) convertView.findViewById(R.id.tvArtistName);
            viewHolder.date = (TextView) convertView.findViewById(R.id.tvDate);
            viewHolder.album = (TextView) convertView.findViewById(R.id.tvAlbumName);




            convertView.setTag(viewHolder);
        }

        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.song.setText("Track: "+track.songName);
        viewHolder.artist.setText("Artist: "+track.artistName);
        viewHolder.album.setText("Album: "+track.albumName);
        viewHolder.date.setText("Date: "+track.date.toString());

        return convertView;
    }

    private static class ViewHolder{
           TextView song;
           TextView album;
           TextView artist;
           TextView date;

    }

}


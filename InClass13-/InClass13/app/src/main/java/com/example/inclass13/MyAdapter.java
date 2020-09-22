package com.example.inclass13;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    ArrayList<City> cityinfolist;

    public MyAdapter(ArrayList<City> cityinfolist) {
        this.cityinfolist = cityinfolist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tripname.setText(cityinfolist.get(position).tripname);
        holder.cityname.setText(cityinfolist.get(position).description);
    }

    @Override
    public int getItemCount() {
        return this.cityinfolist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tripname, cityname;
        ImageView map,add;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tripname = itemView.findViewById(R.id.tripnameitem);
            cityname = itemView.findViewById(R.id.cityitem);

            map = itemView.findViewById(R.id.mapicon);
            add = itemView.findViewById(R.id.addplacesicon);
        }
    }
}

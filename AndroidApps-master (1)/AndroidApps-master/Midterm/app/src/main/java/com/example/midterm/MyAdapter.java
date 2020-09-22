package com.example.midterm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    ArrayList<ForecastClass> forecastlist;

    public MyAdapter(ArrayList<ForecastClass> forecastlist) {
        this.forecastlist = forecastlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ForecastClass forecastClass = this.forecastlist.get(position);

        holder.time.setText(forecastClass.time);
        holder.temp.setText(forecastClass.temp+" F");
        holder.humidity.setText(forecastClass.humidity+"%");
        holder.description.setText(forecastClass.description);

    }

    @Override
    public int getItemCount() {
        return this.forecastlist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView time, temp, humidity, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.timelayout).findViewById(R.id.time);
            temp = itemView.findViewById(R.id.templayout).findViewById(R.id.temp);
            humidity = itemView.findViewById(R.id.humditylayout).findViewById(R.id.humidity);
            description = itemView.findViewById(R.id.humditylayout).findViewById(R.id.description);


        }
    }
}

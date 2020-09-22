package com.example.sampleapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder> {

    ArrayList<String> s;

    public RecycleAdapter(ArrayList<String> s) {
        this.s = s;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return null;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String result = s.get(position);

        holder.tv1.setText(result);
        holder.tv2.setText(result);
    }

    @Override
    public int getItemCount() {
        return this.s.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv1, tv2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv1 = itemView.findViewById(R.id.id1);
            tv2 = itemView.findViewById(R.id.id2);
        }

    }


}

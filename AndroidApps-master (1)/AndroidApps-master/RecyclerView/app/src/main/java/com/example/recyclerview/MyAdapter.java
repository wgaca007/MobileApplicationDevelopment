package com.example.recyclerview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    ArrayList<String> s = new ArrayList<>();
    itemClick onItemClickListener;

    public MyAdapter(ArrayList<String> s, itemClick onItemClickListener) {
        this.s=s;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v, this.onItemClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d("View number: ", position+"");
        String text = s.get(position);

        holder.samplebutton.setText(text);
        holder.sampletext.setText(text);

    }

    @Override
    public int getItemCount() {
        return this.s.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView sampletext;
        Button samplebutton;
        itemClick onItemClickListener;

        public MyViewHolder(@NonNull View itemView, final itemClick onItemClickListener) {

            super(itemView);
            sampletext = itemView.findViewById(R.id.sampletext);
            samplebutton = itemView.findViewById(R.id.samplebutton);
            this.onItemClickListener = onItemClickListener;

            samplebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(getAdapterPosition());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    public interface itemClick {
        void onItemClick(int position);
    }


}

package com.example.inclass11;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    ArrayList<ImageInfo> imageinfolist;
    ImageItemClick onImageItemClicklistener;

    public MyAdapter(ArrayList<ImageInfo> imageinfolist, ImageItemClick onImageItemClick) {
        this.imageinfolist = imageinfolist;
        this.onImageItemClicklistener = onImageItemClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Picasso.get().load(imageinfolist.get(position).url).into(holder.imageitem);
    }

    @Override
    public int getItemCount() {
        return this.imageinfolist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageitem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageitem = itemView.findViewById(R.id.imageitem);
            imageitem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    onImageItemClicklistener.onImageItemClick(imageinfolist.get(getAdapterPosition()));
                    return true;
                }
            });
        }
    }

    interface ImageItemClick{
        void onImageItemClick(ImageInfo imageInfo);
    }
}
